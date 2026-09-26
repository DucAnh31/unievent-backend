package com.ducanh.unievent.service;

import com.ducanh.unievent.common.PageResponse;
import com.ducanh.unievent.common.enums.EventStatus;
import com.ducanh.unievent.dto.request.CreateEventRequest;
import com.ducanh.unievent.dto.request.EventFilterRequest;
import com.ducanh.unievent.dto.request.UpdateEventRequest;
import com.ducanh.unievent.dto.response.EventResponse;
import com.ducanh.unievent.entity.Event;
import com.ducanh.unievent.entity.EventCategory;
import com.ducanh.unievent.entity.User;
import com.ducanh.unievent.exception.ApiException;
import com.ducanh.unievent.exception.ErrorCode;
import com.ducanh.unievent.mapper.EventMapper;
import com.ducanh.unievent.repository.EventCategoryRepository;
import com.ducanh.unievent.repository.EventRepository;
import com.ducanh.unievent.repository.UserRepository;
import com.ducanh.unievent.security.custom.CustomUserDetails;
import com.ducanh.unievent.specification.EventSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.data.autoconfigure.web.DataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {
    private final EventMapper eventMapper;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventCategoryRepository eventCategoryRepository;

    private final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "title",
            "startTime",
            "registrationDeadline",
            "createdAt"
    );

    private User getCurrentUser()
    {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(userDetails.getId()).orElseThrow(
                () -> new ApiException(ErrorCode.USER_NOT_FOUND));
        return user;
    }



    @Transactional
    public EventResponse createEvent(CreateEventRequest request)
    {
        validateEventTime(request.getStartTime(), request.getEndTime(), request.getRegistrationDeadline());

        User user = getCurrentUser();
        EventCategory category = eventCategoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(() -> new ApiException(ErrorCode.EVENT_CATEGORY_NOT_FOUND));

        Event event = eventMapper.toEvent(request);
        event.setOrganizer(user);
        event.setCategory(category);
        event.setStatus(EventStatus.DRAFT);

        return eventMapper.toEventResponse(eventRepository.save(event));
    }

    public List<EventResponse> getMyEvents()
    {
        User organizer = getCurrentUser();
        List<Event> events = eventRepository.findAllByOrganizerId(organizer.getId());

        return  eventMapper.toListEventResponse(events);
    }

    public EventResponse getMyEvent(Long eventId)
    {
        User organizer = getCurrentUser();
        Event event = eventRepository.findByIdAndOrganizerId(eventId, organizer.getId())
                .orElseThrow(() -> new ApiException(ErrorCode.EVENT_NOT_FOUND));
        return eventMapper.toEventResponse(event);
    }

    @Transactional
    public EventResponse updateMyEvent(Long eventId, UpdateEventRequest request)
    {
        User organizer = getCurrentUser();
        Event event = eventRepository.findByIdAndOrganizerId(eventId, organizer.getId())
                .orElseThrow(() -> new ApiException(ErrorCode.EVENT_NOT_FOUND));

        if(event.getStatus() != EventStatus.DRAFT && event.getStatus() != EventStatus.PUBLISHED)
            throw new ApiException(ErrorCode.EVENT_CANNOT_BE_UPDATED);

        validateEventTime(request.getStartTime(), request.getEndTime(), request.getRegistrationDeadline());

        eventMapper.updateEvent(event, request);

        EventCategory category = eventCategoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(() -> new ApiException(ErrorCode.EVENT_CATEGORY_NOT_FOUND));
        event.setCategory(category);

        return eventMapper.toEventResponse(eventRepository.save(event));


    }

    @Transactional
    public void deleteMyEvent(Long eventId)
    {
        User organizer = getCurrentUser();
        Event event = eventRepository.findByIdAndOrganizerId(eventId, organizer.getId())
                .orElseThrow(() -> new ApiException(ErrorCode.EVENT_NOT_FOUND));

        if(event.getStatus() != EventStatus.DRAFT)
            throw new ApiException(ErrorCode.EVENT_CANNOT_BE_DELETED);

        eventRepository.delete(event);

    }

    @Transactional
    public EventResponse publishEvent(Long eventId)
    {
        User organizer = getCurrentUser();
        Event event = eventRepository.findByIdAndOrganizerId(eventId, organizer.getId())
                .orElseThrow(() -> new ApiException(ErrorCode.EVENT_NOT_FOUND));

        if(event.getStatus() != EventStatus.DRAFT ||
                !event.getRegistrationDeadline().isAfter(Instant.now()))
            throw new ApiException(ErrorCode.EVENT_CANNOT_BE_PUBLISH);

        event.setStatus(EventStatus.PUBLISHED);
        return eventMapper.toEventResponse(eventRepository.save(event));
    }

    @Transactional
    public EventResponse closeRegistration(Long eventId)
    {
        User organizer = getCurrentUser();
        Event event = eventRepository.findByIdAndOrganizerId(eventId, organizer.getId())
                .orElseThrow(() -> new ApiException(ErrorCode.EVENT_NOT_FOUND));

        if(event.getStatus() != EventStatus.PUBLISHED)
            throw new ApiException(ErrorCode.EVENT_CANNOT_CLOSE_REGISTRATION);

        event.setStatus(EventStatus.REGISTRATION_CLOSED);
        return eventMapper.toEventResponse(eventRepository.save(event));
    }

    @Transactional
    public EventResponse cancelEvent(Long eventId)
    {
        User organizer = getCurrentUser();
        Event event = eventRepository.findByIdAndOrganizerId(eventId, organizer.getId())
                .orElseThrow(() -> new ApiException(ErrorCode.EVENT_NOT_FOUND));

        if((event.getStatus() != EventStatus.PUBLISHED && event.getStatus() != EventStatus.REGISTRATION_CLOSED) ||
            !event.getStartTime().isAfter(Instant.now()))
            throw new ApiException(ErrorCode.EVENT_CANNOT_CANCEL);

        event.setStatus(EventStatus.CANCELLED);
        return eventMapper.toEventResponse(eventRepository.save(event));

    }

    public EventResponse getPublicEvent(Long eventId)
    {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(()-> new ApiException(ErrorCode.EVENT_NOT_FOUND));
        if(event.getStatus() == EventStatus.DRAFT)
            throw new ApiException(ErrorCode.EVENT_NOT_FOUND);
        return eventMapper.toEventResponse(event);
    }

//    public PageResponse<EventResponse> getPublicEvents(
//            String keyword,
//            Long categoryId,
//            EventStatus status,
//            Instant from,
//            Instant to,
//            int page,
//            int size,
//            String sort
//    )
//    {
//        if (page < 0
//                || size < 1 || size > 50
//                || (categoryId != null && categoryId <= 0)
//                || status == EventStatus.DRAFT
//                || (from != null && to != null && !from.isBefore(to))) {
//            throw new ApiException(ErrorCode.VALIDATION_ERROR);
//        }
//
//        StringTokenizer stringTokenizer = new StringTokenizer(sort, ",");
//        ArrayList<String> part = new ArrayList<>();
//        while (stringTokenizer.hasMoreTokens())
//            part.add(stringTokenizer.nextToken());
//
//        if(part.size() != 2)
//            throw new ApiException(ErrorCode.VALIDATION_ERROR);
//
//        String field = part.get(0).trim();
//        if(!ALLOWED_SORT_FIELDS.contains(field))
//            throw new ApiException(ErrorCode.VALIDATION_ERROR);
//
//        Sort.Direction direction = Sort.Direction.fromOptionalString(part.get(1).trim())
//                .orElseThrow(() -> new ApiException(ErrorCode.VALIDATION_ERROR));
//
//        Sort order = Sort.by(new Sort.Order(direction, field), new Sort.Order(direction, "id"));
//
//
//        String titlePattern = null;
//        if (keyword != null && !keyword.isBlank()) {
//            if (keyword.length() > 100) {
//                throw new ApiException(ErrorCode.VALIDATION_ERROR);
//            }
//
//            String escapedKeyword = keyword.trim()
//                    .toLowerCase()
//                    .replace("!", "!!")
//                    .replace("%", "!%")
//                    .replace("_", "!_");
//
//            titlePattern = "%" + escapedKeyword + "%";
//        }
//
//        Pageable pageable = PageRequest.of(page, size, order);
//
//        return PageResponse.from(
//                eventMapper.toPageEventResponse(eventRepository.findPublicEvents(
//                        EventStatus.DRAFT,
//                        status,
//                        categoryId,
//                        titlePattern,
//                        from,
//                        to,
//                        pageable
//                )));
//    }

    public Page<EventResponse> getPublicEvents(EventFilterRequest filter, int page, int size, String sort)
    {
        if (page < 0 || size < 1 || size > 50) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR);
        }

        if(filter.getStatuses() != null &&
                filter.getStatuses().contains(EventStatus.DRAFT))
                    throw new ApiException(ErrorCode.VALIDATION_ERROR);

        if (filter.getFrom() != null
                && filter.getTo() != null
                && !filter.getFrom().isBefore(filter.getTo())) {
            throw new ApiException(ErrorCode.INVALID_EVENT_TIME);
        }

        Pageable pageable = buildPageable(page, size, sort);

        Specification<Event> specification = EventSpecification.isPublic()
                .and(EventSpecification.hasStatus(filter.getStatuses()))
                .and(EventSpecification.hasCategoryId(filter.getCategoryIds()))
                .and(EventSpecification.hasKeyword(filter.getKeyword()))
                .and(EventSpecification.startFrom(filter.getFrom()))
                .and(EventSpecification.startBefore(filter.getTo()));


        return eventRepository.findAll(specification, pageable)
                .map(event -> eventMapper.toEventResponse(event));

    }

    private Pageable buildPageable(int page, int size, String sort)
    {
        StringTokenizer stringTokenizer = new StringTokenizer(sort, ",");
        ArrayList<String> part = new ArrayList<>();
        while (stringTokenizer.hasMoreTokens())
            part.add(stringTokenizer.nextToken());

        if(part.size() != 2)
            throw new ApiException(ErrorCode.VALIDATION_ERROR);

        String field = part.get(0).trim();

        if(!ALLOWED_SORT_FIELDS.contains(field))
            throw new ApiException(ErrorCode.VALIDATION_ERROR);

        Sort.Direction direction = Sort.Direction.fromOptionalString(part.get(1).trim())
                .orElseThrow(() -> new ApiException(ErrorCode.VALIDATION_ERROR));

        Sort order = Sort.by(new Sort.Order(direction, field), new Sort.Order(direction, "id"));

        return PageRequest.of(page, size, order);
    }



    private void validateEventTime(Instant start, Instant end, Instant registrationDeadline)
    {
        if(!start.isBefore(end) || !start.isAfter(registrationDeadline))
            throw new ApiException(ErrorCode.INVALID_EVENT_TIME);
    }


}
