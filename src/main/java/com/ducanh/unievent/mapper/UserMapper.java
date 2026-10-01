package com.ducanh.unievent.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.ducanh.unievent.dto.request.RegisterRequest;
import com.ducanh.unievent.dto.request.UpdateUserRequest;
import com.ducanh.unievent.dto.response.UserResponse;
import com.ducanh.unievent.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    public User toUser(RegisterRequest request);

    public UserResponse toUserResponse(User user);

    public List<UserResponse> toListUserResponse(List<User> users);

    void updateUser(@MappingTarget User user, UpdateUserRequest request);
}
