package com.ducanh.unievent.service;

import com.ducanh.unievent.dto.request.ChangeUserRoleRequest;
import com.ducanh.unievent.dto.request.ChangeUserStatusRequest;
import com.ducanh.unievent.dto.response.UserResponse;
import com.ducanh.unievent.entity.User;
import com.ducanh.unievent.exception.ApiException;
import com.ducanh.unievent.exception.ErrorCode;
import com.ducanh.unievent.mapper.UserMapper;
import com.ducanh.unievent.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserResponse> getUsers()
    {
        List<User> users = userRepository.findAll();
        return userMapper.toListUserResponse(users);
    }

    public UserResponse getUser(Long id)
    {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ApiException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    @Transactional
    public UserResponse changeUserStatus(ChangeUserStatusRequest request, Long id)
    {
        User user = userRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        //fix: admin khong the thay doi status cua admin khac

        user.setStatus(request.getStatus());

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse changeUserRole(ChangeUserRoleRequest request, Long id)
    {
        User user = userRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        user.setRole(request.getRole());

        return userMapper.toUserResponse(userRepository.save(user));
    }
}
