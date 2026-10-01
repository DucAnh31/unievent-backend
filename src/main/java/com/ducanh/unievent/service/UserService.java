package com.ducanh.unievent.service;

import com.ducanh.unievent.dto.request.ChangePasswordRequest;
import com.ducanh.unievent.dto.request.UpdateUserRequest;
import com.ducanh.unievent.dto.response.UserResponse;
import com.ducanh.unievent.entity.User;
import com.ducanh.unievent.exception.ApiException;
import com.ducanh.unievent.exception.ErrorCode;
import com.ducanh.unievent.mapper.UserMapper;
import com.ducanh.unievent.repository.UserRepository;
import com.ducanh.unievent.security.SecurityHelper;
import com.ducanh.unievent.security.custom.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SecurityHelper securityHelper;
    private final PasswordEncoder encoder;

//    private User getCurrentUser()
//    {
//        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User user = userRepository.findById(userDetails.getId()).orElseThrow(
//                () -> new ApiException(ErrorCode.USER_NOT_FOUND));
//        return user;
//    }

    public UserResponse getMyProfile()
    {
        User user = securityHelper.getCurrentUser();
        return userMapper.toUserResponse(user);
    }


    @Transactional
    public UserResponse updateMyProfile(UpdateUserRequest request)
    {
        User user = securityHelper.getCurrentUser();
        userMapper.updateUser(user, request);

        try {
            user = userRepository.saveAndFlush(user);
        }
        catch (DataIntegrityViolationException e)
        {
            throw new ApiException(ErrorCode.EMAIL_EXISTED);
        }

        return userMapper.toUserResponse(user);
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request)
    {
        User user = securityHelper.getCurrentUser();
        if(encoder.matches(request.getCurrentPassword(), user.getPassword()))

        user.setPassword(encoder.encode(request.getPassword()));
        userRepository.save(user);
    }
}
