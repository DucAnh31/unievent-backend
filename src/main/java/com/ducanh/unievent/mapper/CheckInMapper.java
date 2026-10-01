package com.ducanh.unievent.mapper;

import com.ducanh.unievent.dto.request.CreateEventCategoryRequest;
import com.ducanh.unievent.dto.request.UpdateEventCategoryRequest;
import com.ducanh.unievent.dto.response.CheckInResponse;
import com.ducanh.unievent.dto.response.EventCategoryResponse;
import com.ducanh.unievent.entity.CheckIn;
import com.ducanh.unievent.entity.EventCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.lang.annotation.Target;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CheckInMapper {

    @Mapping(target = "registrationId", source = "registration.id")
    @Mapping(target = "studentName", source = "registration.user.fullName")
    @Mapping(target = "studentCode", source = "registration.user.studentCode")
    public CheckInResponse toCheckInResponse(CheckIn checkIn);

    public List<CheckInResponse> toListCheckInResponse(List<CheckIn> checkIns);

}
