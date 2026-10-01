package com.ducanh.unievent.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ducanh.unievent.dto.response.EventRegistrationResponse;
import com.ducanh.unievent.entity.Registration;

@Mapper(componentModel = "spring")
public interface EventRegistrationMapper {

    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "userId", source = "user.id")
    public EventRegistrationResponse toRegistrationEventResponse(Registration registration);

    public List<EventRegistrationResponse> toListEventRegistrationResponse(List<Registration> registrations);
}
