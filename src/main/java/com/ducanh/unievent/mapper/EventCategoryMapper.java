package com.ducanh.unievent.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.ducanh.unievent.dto.request.CreateEventCategoryRequest;
import com.ducanh.unievent.dto.request.UpdateEventCategoryRequest;
import com.ducanh.unievent.dto.response.EventCategoryResponse;
import com.ducanh.unievent.entity.EventCategory;

@Mapper(componentModel = "spring")
public interface EventCategoryMapper {
    public EventCategory toEventCategory(CreateEventCategoryRequest request);

    public EventCategoryResponse toEventCategoryResponse(EventCategory eventCategory);

    public List<EventCategoryResponse> toListEventCategoryResponse(List<EventCategory> eventCategories);

    void updateEventCategory(@MappingTarget EventCategory eventCategory, UpdateEventCategoryRequest request);
}
