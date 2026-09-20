package com.ducanh.unievent.mapper;

import com.ducanh.unievent.dto.request.RegisterRequest;
import com.ducanh.unievent.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    public User toUser(RegisterRequest request);
}
