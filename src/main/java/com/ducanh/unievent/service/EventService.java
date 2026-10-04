package com.ducanh.unievent.service;

import java.time.Instant;
import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ducanh.unievent.common.enums.EventStatus;
import com.ducanh.unievent.common.enums.RegistrationStatus;
import com.ducanh.unievent.common.util.PageableFactoryUtil;
import com.ducanh.unievent.dto.request.CreateEventRequest;
import com.ducanh.unievent.dto.request.EventFilterRequest;
import com.ducanh.unievent.dto.request.UpdateEventRequest;
import com.ducanh.unievent.dto.response.EventResponse;
import com.ducanh.unievent.dto.response.EventStatisticsResponse;
import com.ducanh.unievent.entity.Event;
import com.ducanh.unievent.entity.EventCategory;
import com.ducanh.unievent.entity.User;
import com.ducanh.unievent.exception.ApiException;
import com.ducanh.unievent.exception.ErrorCode;
import com.ducanh.unievent.mapper.EventMapper;
import com.ducanh.unievent.repository.*;
import com.ducanh.unievent.security.SecurityHelper;
import com.ducanh.unievent.specification.EventSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventMapper eventMapper;
    private final EventRepository eventRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final SecurityHelper securityHelper;
    private final CheckInRepository checkInRepository;
    private final EventRegistrationRepository eventRegistrationRepository;

    private final Set<String> ALLOWED_EVENTS_SORT_FIELDS =
            Set.of("title", "startTime", "registrationDeadline", "createdAt");

    @Transactional
    public EventResponse createEvent(CreateEventRequest request) {
        validateEventTime(request.getStartTime(), request.getEndTime(), request.getRegistrationDeadline());

        User user = securityHelper.getCurrentUser();
        EventCategory category = eventCategoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(() -> new ApiException(ErrorCode.EVENT_CATEGORY_NOT_FOUND));

        Event event = eventMapper.toEvent(request);
        event.setOrganizer(user);
        event.setCategory(category);
        event.setStatus(EventStatus.DRAFT);

        return eventMapper.toEventResponse(eventRepository.save(event));
    }

    public Page<EventResponse> getMyEvents(EventFilterRequest filter, int page, int size, String sort) {

        if (filter.getFrom() != null
                && filter.getTo() != null
                && !filter.getFrom().isBefore(filter.getTo())) {
            throw new ApiException(ErrorCode.INVALID_EVENT_TIME);
        }
        User organizer = securityHelper.getCurrentUser();
        Pageable pageable = PageableFactoryUtil.create(page, size, sort, ALLOWED_EVENTS_SORT_FIELDS);

        Specification<Event> specification =
                EventSpecification.buildOrganizerEventSpecification(filter, organizer.getId());

        return eventRepository.findAll(specification, pageable).map(event -> eventMapper.toEventResponse(event));
    }

    public EventResponse getMyEvent(Long eventId) {
        User organizer = securityHelper.getCurrentUser();
        Event event = eventRepository
                .findByIdAndOrganizerId(eventId, organizer.getId())
                .orElseThrow(() -> new ApiException(ErrorCode.EVENT_NOT_FOUND));
        return eventMapper.toEventResponse(event);
    }

    @Transactional
    public EventResponse updateMyEvent(Long eventId, UpdateEventRequest request) {
        User organizer = securityHelper.getCurrentUser();
        Event event = eventRepository
                .findByIdAndOrganizerId(eventId, organizer.getId())
                .orElseThrow(() -> new ApiException(ErrorCode.EVENT_NOT_FOUND));

        if (event.getStatus() != EventStatus.DRAFT) throw new ApiException(ErrorCode.EVENT_CANNOT_BE_UPDATED);

        validateEventTime(request.getStartTime(), request.getEndTime(), request.getRegistrationDeadline());

        eventMapper.updateEvent(event, request);

        EventCategory category = eventCategoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(() -> new ApiException(ErrorCode.EVENT_CATEGORY_NOT_FOUND));
        event.setCategory(category);

        return eventMapper.toEventResponse(eventRepository.save(event));
    }

    @Transactional
    public void deleteMyEvent(Long eventId) {
        User organizer = securityHelper.getCurrentUser();
        Event event = eventRepository
                .findByIdAndOrganizerId(eventId, organizer.getId())
                .orElseThrow(() -> new ApiException(ErrorCode.EVENT_NOT_FOUND));

        if (event.getStatus() != EventStatus.DRAFT) throw new ApiException(ErrorCode.EVENT_CANNOT_BE_DELETED);

        eventRepository.delete(event);
    }

    @Transactional
    public EventResponse submitEventForApproval(Long eventId) {
        User organizer = securityHelper.getCurrentUser();
        Event event = eventRepository
                .findByIdAndOrganizerId(eventId, organizer.getId())
                .orElseThrow(() -> new ApiException(ErrorCode.EVENT_NOT_FOUND));

        if (event.getStatus() != EventStatus.DRAFT
                || !event.getRegistrationDeadline().isAfter(Instant.now()))
            throw new ApiException(ErrorCode.EVENT_CANNOT_BE_PUBLISH);

        event.setStatus(EventStatus.PENDING_APPROVAL);
        return eventMapper.toEventResponse(eventRepository.save(event));
    }

    @Transactional
    public EventResponse closeRegistration(Long eventId) {
        User organizer = securityHelper.getCurrentUser();
        Event event = eventRepository
                .findByIdAndOrganizerId(eventId, organizer.getId())
                .orElseThrow(() -> new ApiException(ErrorCode.EVENT_NOT_FOUND));

        if (event.getStatus() != EventStatus.PUBLISHED)
            throw new ApiException(ErrorCode.EVENT_CANNOT_CLOSE_REGISTRATION);

        event.setStatus(EventStatus.REGISTRATION_CLOSED);
        return eventMapper.toEventResponse(eventRepository.save(event));
    }

    @Transactional
    public EventResponse cancelEvent(Long eventId) {
        User organizer = securityHelper.getCurrentUser();
        Event event = eventRepository
                .findByIdAndOrganizerId(eventId, organizer.getId())
                .orElseThrow(() -> new ApiException(ErrorCode.EVENT_NOT_FOUND));

        if ((event.getStatus() != EventStatus.PUBLISHED && event.getStatus() != EventStatus.REGISTRATION_CLOSED)
                || !event.getStartTime().isAfter(Instant.now())) throw new ApiException(ErrorCode.EVENT_CANNOT_CANCEL);

        event.setStatus(EventStatus.CANCELLED);
        return eventMapper.toEventResponse(eventRepository.save(event));
    }

    @Transactional
    public EventResponse cancelAdminEvent(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ApiException(ErrorCode.EVENT_NOT_FOUND));

        if ((event.getStatus() != EventStatus.PUBLISHED && event.getStatus() != EventStatus.REGISTRATION_CLOSED)
                || !event.getStartTime().isAfter(Instant.now())) throw new ApiException(ErrorCode.EVENT_CANNOT_CANCEL);

        event.setStatus(EventStatus.CANCELLED);
        return eventMapper.toEventResponse(eventRepository.save(event));
    }

    public EventResponse getPublicEvent(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ApiException(ErrorCode.EVENT_NOT_FOUND));
        if (event.getStatus() == EventStatus.DRAFT || event.getStatus() == EventStatus.PENDING_APPROVAL)
            throw new ApiException(ErrorCode.EVENT_NOT_FOUND);
        return eventMapper.toEventResponse(event);
    }

    public EventResponse getAdminEvent(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ApiException(ErrorCode.EVENT_NOT_FOUND));

        return eventMapper.toEventResponse(event);
    }

    public Page<EventResponse> getPublicEvents(EventFilterRequest filter, int page, int size, String sort) {

        if (filter.getStatuses() != null
                && (filter.getStatuses().contains(EventStatus.DRAFT)
                        || filter.getStatuses().contains(EventStatus.PENDING_APPROVAL)))
            throw new ApiException(ErrorCode.VALIDATION_ERROR);

        if (filter.getFrom() != null
                && filter.getTo() != null
                && !filter.getFrom().isBefore(filter.getTo())) {
            throw new ApiException(ErrorCode.INVALID_EVENT_TIME);
        }

        Pageable pageable = PageableFactoryUtil.create(page, size, sort, ALLOWED_EVENTS_SORT_FIELDS);

        Specification<Event> specification = EventSpecification.buildEventSpecification(filter);

        return eventRepository.findAll(specification, pageable).map(event -> eventMapper.toEventResponse(event));
    }

    public Page<EventResponse> getAdminEvents(EventFilterRequest filter, int page, int size, String sort) {

        if (filter.getFrom() != null
                && filter.getTo() != null
                && !filter.getFrom().isBefore(filter.getTo())) {
            throw new ApiException(ErrorCode.INVALID_EVENT_TIME);
        }

        Pageable pageable = PageableFactoryUtil.create(page, size, sort, ALLOWED_EVENTS_SORT_FIELDS);

        Specification<Event> specification = EventSpecification.buildAdminEventSpecification(filter);

        return eventRepository.findAll(specification, pageable).map(event -> eventMapper.toEventResponse(event));
    }

    private void validateEventTime(Instant start, Instant end, Instant registrationDeadline) {
        if (!start.isBefore(end) || registrationDeadline.isAfter(start))
            throw new ApiException(ErrorCode.INVALID_EVENT_TIME);
    }

    public EventStatisticsResponse getMyEventStatistics(Long eventId) {
        User organizer = securityHelper.getCurrentUser();

        Event event = eventRepository
                .findByIdAndOrganizerId(eventId, organizer.getId())
                .orElseThrow(() -> new ApiException(ErrorCode.EVENT_NOT_FOUND));
        return EventStatisticsResponse.builder()
                .eventId(eventId)
                .checkedInCount(checkInRepository.countByRegistration_Event_Id(eventId))
                .maxParticipants(event.getMaxParticipants())
                .registeredCount(
                        eventRegistrationRepository.countByEvent_idAndStatus(eventId, RegistrationStatus.REGISTERED))
                .build();
    }

    @Transactional
    public EventResponse approveEvent(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ApiException(ErrorCode.EVENT_NOT_FOUND));

        if (event.getStatus() != EventStatus.PENDING_APPROVAL
                || !Instant.now().isBefore(event.getRegistrationDeadline()))
            throw new ApiException(ErrorCode.EVENT_CANNOT_BE_APPROVED);

        event.setStatus(EventStatus.PUBLISHED);
        return eventMapper.toEventResponse(eventRepository.save(event));
    }

    @Transactional
    public EventResponse rejectEvent(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ApiException(ErrorCode.EVENT_NOT_FOUND));

        if (event.getStatus() != EventStatus.PENDING_APPROVAL)
            throw new ApiException(ErrorCode.EVENT_CANNOT_BE_REJECTED);

        event.setStatus(EventStatus.DRAFT);

        return eventMapper.toEventResponse(eventRepository.save(event));
    }
}
