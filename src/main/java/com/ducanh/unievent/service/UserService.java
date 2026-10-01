package com.ducanh.unievent.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ducanh.unievent.dto.request.ChangePasswordRequest;
import com.ducanh.unievent.dto.request.UpdateUserRequest;
import com.ducanh.unievent.dto.response.UserResponse;
import com.ducanh.unievent.entity.User;
import com.ducanh.unievent.exception.ApiException;
import com.ducanh.unievent.exception.ErrorCode;
import com.ducanh.unievent.mapper.UserMapper;
import com.ducanh.unievent.repository.UserRepository;
import com.ducanh.unievent.security.SecurityHelper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SecurityHelper securityHelper;
    private final PasswordEncoder encoder;

    public UserResponse getMyProfile() {
        User user = securityHelper.getCurrentUser();
        return userMapper.toUserResponse(user);
    }

    @Transactional
    public UserResponse updateMyProfile(UpdateUserRequest request) {
        User user = securityHelper.getCurrentUser();
        userMapper.updateUser(user, request);

        try {
            user = userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException e) {
            throw new ApiException(ErrorCode.EMAIL_EXISTED);
        }

        return userMapper.toUserResponse(user);
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        User user = securityHelper.getCurrentUser();
        if (!encoder.matches(request.getCurrentPassword(), user.getPassword()))
            throw new ApiException(ErrorCode.INVALID_CURRENT_PASSWORD);
        user.setPassword(encoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
