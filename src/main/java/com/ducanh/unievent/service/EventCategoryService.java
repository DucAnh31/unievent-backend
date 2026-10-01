package com.ducanh.unievent.service;

import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ducanh.unievent.common.util.PageableFactoryUtil;
import com.ducanh.unievent.dto.request.CreateEventCategoryRequest;
import com.ducanh.unievent.dto.request.UpdateEventCategoryRequest;
import com.ducanh.unievent.dto.response.EventCategoryResponse;
import com.ducanh.unievent.entity.EventCategory;
import com.ducanh.unievent.exception.ApiException;
import com.ducanh.unievent.exception.ErrorCode;
import com.ducanh.unievent.mapper.EventCategoryMapper;
import com.ducanh.unievent.repository.EventCategoryRepository;
import com.ducanh.unievent.repository.EventRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventCategoryService {
    private final EventCategoryRepository eventCategoryRepository;
    private final EventCategoryMapper eventCategoryMapper;
    private final EventRepository eventRepository;
    private static final Set<String> ALLOWED_CATEGORY_SORT_FIELDS = Set.of("name", "createdAt");

    public EventCategoryResponse getEventCategory(Long eventCategoryId) {
        return eventCategoryMapper.toEventCategoryResponse(eventCategoryRepository
                .findById(eventCategoryId)
                .orElseThrow(() -> new ApiException(ErrorCode.EVENT_CATEGORY_NOT_FOUND)));
    }

    public Page<EventCategoryResponse> getEventCategories(int page, int size, String sort, String keyword) {

        Pageable pageable = PageableFactoryUtil.create(page, size, sort, ALLOWED_CATEGORY_SORT_FIELDS);

        keyword = (keyword == null) ? "" : keyword.trim();

        return eventCategoryRepository
                .findByNameContainingIgnoreCase(keyword, pageable)
                .map(eventCategory -> eventCategoryMapper.toEventCategoryResponse(eventCategory));
    }

    @Transactional
    public EventCategoryResponse createEventCategory(CreateEventCategoryRequest request) {
        EventCategory eventCategory = eventCategoryMapper.toEventCategory(request);
        try {
            eventCategory = eventCategoryRepository.saveAndFlush(eventCategory);
        } catch (DataIntegrityViolationException e) {
            throw new ApiException(ErrorCode.EVENT_CATEGORY_EXISTED);
        }

        return eventCategoryMapper.toEventCategoryResponse(eventCategory);
    }

    @Transactional
    public EventCategoryResponse updateEventCategory(Long eventCategoryId, UpdateEventCategoryRequest request) {
        EventCategory eventCategory = eventCategoryRepository
                .findById(eventCategoryId)
                .orElseThrow(() -> new ApiException(ErrorCode.EVENT_CATEGORY_NOT_FOUND));

        eventCategoryMapper.updateEventCategory(eventCategory, request);

        try {
            eventCategory = eventCategoryRepository.saveAndFlush(eventCategory);
        } catch (DataIntegrityViolationException e) {
            throw new ApiException(ErrorCode.EVENT_CATEGORY_EXISTED);
        }

        return eventCategoryMapper.toEventCategoryResponse(eventCategory);
    }

    @Transactional
    public void deleteEventCategory(Long eventCategoryId) {
        EventCategory category = eventCategoryRepository
                .findById(eventCategoryId)
                .orElseThrow(() -> new ApiException(ErrorCode.EVENT_CATEGORY_NOT_FOUND));

        if (eventRepository.existsByCategory_Id(eventCategoryId))
            throw new ApiException(ErrorCode.EVENT_CATEGORY_IN_USE);

        eventCategoryRepository.delete(category);
    }
}
