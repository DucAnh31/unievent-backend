package com.ducanh.unievent.service;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ducanh.unievent.common.enums.EventStatus;
import com.ducanh.unievent.common.enums.RegistrationStatus;
import com.ducanh.unievent.common.util.PageableFactoryUtil;
import com.ducanh.unievent.dto.request.RegistrationFilterRequest;
import com.ducanh.unievent.dto.response.CheckInCodeResponse;
import com.ducanh.unievent.dto.response.EventRegistrationResponse;
import com.ducanh.unievent.entity.Event;
import com.ducanh.unievent.entity.Registration;
import com.ducanh.unievent.entity.User;
import com.ducanh.unievent.exception.ApiException;
import com.ducanh.unievent.exception.ErrorCode;
import com.ducanh.unievent.mapper.EventRegistrationMapper;
import com.ducanh.unievent.repository.EventRegistrationRepository;
import com.ducanh.unievent.repository.EventRepository;
import com.ducanh.unievent.security.SecurityHelper;
import com.ducanh.unievent.specification.RegistrationSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventRegistrationService {
    private final SecurityHelper securityHelper;
    private final EventRepository eventRepository;
    private final EventRegistrationRepository eventRegistrationRepository;
    private final EventRegistrationMapper eventRegistrationMapper;
    private static final Set<String> ALLOWED_REGISTRATION_SORT_FIELDS = Set.of("registeredAt");

    @Transactional
    public EventRegistrationResponse registerEvent(Long eventId) {
        User user = securityHelper.getCurrentUser();

        Event event = eventRepository
                .findForRegistrationById(eventId)
                .orElseThrow(() -> new ApiException(ErrorCode.EVENT_NOT_FOUND));

        if (event.getStatus() != EventStatus.PUBLISHED) throw new ApiException(ErrorCode.REGISTRATION_NOT_OPEN);

        if (!Instant.now().isBefore(event.getRegistrationDeadline()))
            throw new ApiException(ErrorCode.REGISTRATION_DEADLINE_PASSED);

        Registration exist = eventRegistrationRepository
                .findByUser_IdAndEvent_Id(user.getId(), eventId)
                .orElse(null);

        if (exist != null && exist.getStatus() == RegistrationStatus.REGISTERED)
            throw new ApiException(ErrorCode.REGISTRATION_ALREADY_REGISTERED);

        Long registeredCount =
                eventRegistrationRepository.countByEvent_idAndStatus(eventId, RegistrationStatus.REGISTERED);

        if (registeredCount >= event.getMaxParticipants()) throw new ApiException(ErrorCode.EVENT_FULL);

        Registration registration = null;
        if (exist != null) {
            registration = exist;
        } else {
            registration = new Registration();
            registration.setEvent(event);
            registration.setUser(user);
        }

        registration.setStatus(RegistrationStatus.REGISTERED);
        registration.setRegisteredAt(Instant.now());
        registration.setStatus(RegistrationStatus.REGISTERED);
        registration.setCancelledAt(null);
        registration.setCheckInCode(UUID.randomUUID().toString());

        return eventRegistrationMapper.toRegistrationEventResponse(eventRegistrationRepository.save(registration));
    }

    @Transactional
    public void cancelRegistration(Long eventId) {
        User user = securityHelper.getCurrentUser();

        Event event = eventRepository
                .findForRegistrationById(eventId)
                .orElseThrow(() -> new ApiException(ErrorCode.EVENT_NOT_FOUND));

        Registration registration = eventRegistrationRepository
                .findByUser_IdAndEvent_Id(user.getId(), eventId)
                .orElseThrow(() -> new ApiException(ErrorCode.REGISTRATION_NOT_FOUND));

        if (registration.getStatus() == RegistrationStatus.CANCELLED)
            throw new ApiException(ErrorCode.REGISTRATION_ALREADY_CANCELLED);

        if ((event.getStatus() != EventStatus.PUBLISHED && event.getStatus() != EventStatus.REGISTRATION_CLOSED)
                || (!Instant.now().isBefore(event.getStartTime())))
            throw new ApiException(ErrorCode.REGISTRATION_CANNOT_CANCEL);

        registration.setCancelledAt(Instant.now());
        registration.setStatus(RegistrationStatus.CANCELLED);

        eventRegistrationRepository.save(registration);
    }

    public EventRegistrationResponse getMyRegistration(Long registrationId) {
        User user = securityHelper.getCurrentUser();

        Registration registration = eventRegistrationRepository
                .findByIdAndUser_Id(registrationId, user.getId())
                .orElseThrow(() -> new ApiException(ErrorCode.REGISTRATION_NOT_FOUND));

        return eventRegistrationMapper.toRegistrationEventResponse(registration);
    }

    public Page<EventRegistrationResponse> getMyRegistrations(int page, int size, String sort) {
        User user = securityHelper.getCurrentUser();

        Pageable pageable = PageableFactoryUtil.create(page, size, sort, ALLOWED_REGISTRATION_SORT_FIELDS);

        return eventRegistrationRepository
                .findAllByUser_Id(user.getId(), pageable)
                .map(registration -> eventRegistrationMapper.toRegistrationEventResponse(registration));
    }

    public CheckInCodeResponse getMyCheckInCode(Long registrationId) {
        User user = securityHelper.getCurrentUser();

        Registration registration = eventRegistrationRepository
                .findById(registrationId)
                .orElseThrow(() -> new ApiException(ErrorCode.REGISTRATION_NOT_FOUND));

        if (registration.getStatus() == RegistrationStatus.CANCELLED) {
            throw new ApiException(ErrorCode.REGISTRATION_ALREADY_CANCELLED);
        }

        return CheckInCodeResponse.builder()
                .checkInCode(registration.getCheckInCode())
                .build();
    }

    public Page<EventRegistrationResponse> getEventRegistrations(
            Long eventId, RegistrationFilterRequest filter, int page, int size, String sort) {
        User organizer = securityHelper.getCurrentUser();

        Event event = eventRepository
                .findByIdAndOrganizerId(eventId, organizer.getId())
                .orElseThrow(() -> new ApiException(ErrorCode.EVENT_NOT_FOUND));

        Pageable pageable = PageableFactoryUtil.create(page, size, sort, ALLOWED_REGISTRATION_SORT_FIELDS);

        Specification<Registration> specification =
                RegistrationSpecification.buildRegistrationSpecification(eventId, filter);

        return eventRegistrationRepository
                .findAll(specification, pageable)
                .map(registration -> eventRegistrationMapper.toRegistrationEventResponse(registration));
    }

    @Transactional
    public void cancelRegistrationByOrganizer(Long eventId, Long registrationId) {
        User organizer = securityHelper.getCurrentUser();

        Event event = eventRepository
                .findForRegistrationByIdAndOrganizer_Id(eventId, organizer.getId())
                .orElseThrow(() -> new ApiException(ErrorCode.EVENT_NOT_FOUND));

        Registration registration = eventRegistrationRepository
                .findByIdAndEvent_Id(registrationId, eventId)
                .orElseThrow(() -> new ApiException(ErrorCode.REGISTRATION_NOT_FOUND));

        if (registration.getStatus() == RegistrationStatus.CANCELLED)
            throw new ApiException(ErrorCode.REGISTRATION_ALREADY_CANCELLED);

        if ((event.getStatus() != EventStatus.PUBLISHED && event.getStatus() != EventStatus.REGISTRATION_CLOSED)
                || !Instant.now().isBefore(event.getStartTime())) {
            throw new ApiException(ErrorCode.REGISTRATION_CANNOT_CANCEL);
        }

        registration.setStatus(RegistrationStatus.CANCELLED);
        registration.setCancelledAt(Instant.now());
    }
}
