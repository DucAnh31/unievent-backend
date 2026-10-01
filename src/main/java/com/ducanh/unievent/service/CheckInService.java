package com.ducanh.unievent.service;

import com.ducanh.unievent.common.enums.EventStatus;
import com.ducanh.unievent.common.enums.RegistrationStatus;
import com.ducanh.unievent.dto.request.CheckInRequest;
import com.ducanh.unievent.dto.response.CheckInCodeResponse;
import com.ducanh.unievent.dto.response.CheckInResponse;
import com.ducanh.unievent.dto.response.StudentCheckInStatusResponse;
import com.ducanh.unievent.entity.CheckIn;
import com.ducanh.unievent.entity.Event;
import com.ducanh.unievent.entity.Registration;
import com.ducanh.unievent.entity.User;
import com.ducanh.unievent.exception.ApiException;
import com.ducanh.unievent.exception.ErrorCode;
import com.ducanh.unievent.mapper.CheckInMapper;
import com.ducanh.unievent.repository.CheckInRepository;
import com.ducanh.unievent.repository.EventRegistrationRepository;
import com.ducanh.unievent.repository.EventRepository;
import com.ducanh.unievent.security.SecurityHelper;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.DialectOverride;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckInService {
    private final SecurityHelper securityHelper;
    private final EventRegistrationRepository eventRegistrationRepository;
    private final EventRepository eventRepository;
    private final CheckInRepository checkInRepository;
    private final CheckInMapper checkInMapper;

    @Transactional
    public CheckInResponse checkIn(Long eventId, CheckInRequest request)
    {
        User organizer = securityHelper.getCurrentUser();

        Event event = eventRepository
                .findForRegistrationByIdAndOrganizer_Id(eventId, organizer.getId())
                .orElseThrow(() -> new ApiException(ErrorCode.EVENT_NOT_FOUND));

        Instant now = Instant.now();
        EventStatus status = event.getStatus();

        boolean validStatus = status == EventStatus.PUBLISHED
                || status == EventStatus.REGISTRATION_CLOSED
                || status == EventStatus.ONGOING;

//        chay test phai cmt nay lai....
//        if (!validStatus
//                || now.isBefore(event.getStartTime())
//                || !now.isBefore(event.getEndTime())) {
//            throw new ApiException(ErrorCode.CHECK_IN_NOT_ALLOWED);
//        }


        Registration registration = eventRegistrationRepository
                .findByEvent_IdAndCheckInCode(eventId, request.getCheckInCode())
                .orElseThrow(() -> new ApiException(ErrorCode.CHECK_IN_CODE_INVALID));

        if (registration.getStatus() != RegistrationStatus.REGISTERED) {
            throw new ApiException(ErrorCode.CHECK_IN_NOT_ALLOWED);
        }

        CheckIn checkIn = CheckIn.builder()
                .checkInTime(now)
                .registration(registration)
                .build();

        try {
            return checkInMapper.toCheckInResponse(checkInRepository.saveAndFlush(checkIn));
        }
        catch (DataIntegrityViolationException e)
        {
            throw new ApiException(ErrorCode.ALREADY_CHECKED_IN);
        }

    }



    public StudentCheckInStatusResponse getMyCheckInStatus(Long registrationId)
    {
        User user = securityHelper.getCurrentUser();

        Registration registration = eventRegistrationRepository
                .findByIdAndUser_Id(registrationId, user.getId())
                .orElseThrow(() -> new ApiException(ErrorCode.REGISTRATION_NOT_FOUND));

        Optional<CheckIn> checkIn = checkInRepository.findByRegistration_Id(registrationId);
        if(checkIn.isEmpty())
        {
            return StudentCheckInStatusResponse.builder()
                    .checkedIn(false)
                    .build();
        }
        else
        {
            return StudentCheckInStatusResponse.builder()
                    .checkedIn(true)
                    .checkInTime(checkIn.get().getCheckInTime())
                    .build();
        }
    }

    public List<CheckInResponse> getEventCheckIns(Long eventId)
    {
        User organizer = securityHelper.getCurrentUser();
        Event event = eventRepository.findByIdAndOrganizerId(eventId, organizer.getId())
                .orElseThrow(() -> new ApiException(ErrorCode.EVENT_NOT_FOUND));

        List<CheckIn> checkIns = checkInRepository.findByRegistration_Event_Id(eventId);

        return checkInMapper.toListCheckInResponse(checkIns);

    }
}
