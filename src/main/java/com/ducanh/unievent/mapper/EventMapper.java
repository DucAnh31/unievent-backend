package com.ducanh.unievent.mapper;

import com.ducanh.unievent.dto.request.CreateEventRequest;
import com.ducanh.unievent.dto.request.RegisterRequest;
import com.ducanh.unievent.dto.request.UpdateEventRequest;
import com.ducanh.unievent.dto.request.UpdateUserRequest;
import com.ducanh.unievent.dto.response.EventResponse;
import com.ducanh.unievent.dto.response.UserResponse;
import com.ducanh.unievent.entity.Event;
import com.ducanh.unievent.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

import java.lang.annotation.Target;
import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {
    public Event toEvent(CreateEventRequest request);

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "organizerId", source = "organizer.id")
    @Mapping(target = "organizerName", source = "organizer.fullName")
    public EventResponse toEventResponse(Event event);

    public List<EventResponse> toListEventResponse(List<Event> events);


    public void updateEvent(@MappingTarget Event event, UpdateEventRequest request);
}
