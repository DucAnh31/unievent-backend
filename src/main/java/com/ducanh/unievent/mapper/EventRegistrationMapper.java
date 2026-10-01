package com.ducanh.unievent.mapper;

import com.ducanh.unievent.common.enums.RegistrationStatus;
import com.ducanh.unievent.dto.response.EventRegistrationResponse;
import com.ducanh.unievent.dto.response.OrganizerRegistrationResponse;
import com.ducanh.unievent.entity.Registration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventRegistrationMapper {


    @Mapping(target = "eventId", source = "event.id")
    public EventRegistrationResponse toRegistrationEventResponse(Registration registration);

    public List<EventRegistrationResponse> toListEventRegistrationResponse(List<Registration> registrations);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "fullName", source = "user.fullName")
    @Mapping(target = "studentCode", source = "user.studentCode")
    OrganizerRegistrationResponse toOrganizerRegistrationResponse(Registration registration);

    List<OrganizerRegistrationResponse> toOrganizerRegistrationResponses(List<Registration> registrations);


}
