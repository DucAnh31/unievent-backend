package com.ducanh.unievent.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ducanh.unievent.dto.response.CheckInResponse;
import com.ducanh.unievent.entity.CheckIn;

@Mapper(componentModel = "spring")
public interface CheckInMapper {

    @Mapping(target = "registrationId", source = "registration.id")
    @Mapping(target = "studentName", source = "registration.user.fullName")
    @Mapping(target = "studentCode", source = "registration.user.studentCode")
    public CheckInResponse toCheckInResponse(CheckIn checkIn);

    public List<CheckInResponse> toListCheckInResponse(List<CheckIn> checkIns);
}
