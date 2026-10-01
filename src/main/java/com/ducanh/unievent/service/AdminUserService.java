package com.ducanh.unievent.service;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ducanh.unievent.common.enums.UserRole;
import com.ducanh.unievent.common.util.PageableFactoryUtil;
import com.ducanh.unievent.dto.request.ChangeUserRoleRequest;
import com.ducanh.unievent.dto.request.ChangeUserStatusRequest;
import com.ducanh.unievent.dto.request.UserFilterRequest;
import com.ducanh.unievent.dto.response.UserResponse;
import com.ducanh.unievent.entity.User;
import com.ducanh.unievent.exception.ApiException;
import com.ducanh.unievent.exception.ErrorCode;
import com.ducanh.unievent.mapper.UserMapper;
import com.ducanh.unievent.repository.UserRepository;
import com.ducanh.unievent.security.SecurityHelper;
import com.ducanh.unievent.specification.UserSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminUserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SecurityHelper securityHelper;
    private static final Set<String> ALLOWED_USER_SORT_FIELDS =
            Set.of("id", "createdAt", "username", "fullName", "email", "studentCode", "role", "status");

    public Page<UserResponse> getUsers(UserFilterRequest filter, int page, int size, String sort) {
        Pageable pageable = PageableFactoryUtil.create(page, size, sort, ALLOWED_USER_SORT_FIELDS);
        Specification<User> specification = UserSpecification.buildUserSpecification(filter);

        return userRepository.findAll(specification, pageable).map(user -> userMapper.toUserResponse(user));
    }

    public UserResponse getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    @Transactional
    public UserResponse changeUserStatus(ChangeUserStatusRequest request, Long id) {
        User currentUser = securityHelper.getCurrentUser();

        User user = userRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        if (currentUser.getId() == user.getId()) throw new ApiException(ErrorCode.CANNOT_MODIFY_YOURSELF);
        if (user.getRole() == UserRole.ADMIN) throw new ApiException(ErrorCode.CANNOT_MODIFY_OTHER_ADMIN);

        user.setStatus(request.getStatus());

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse changeUserRole(ChangeUserRoleRequest request, Long id) {
        User currentUser = securityHelper.getCurrentUser();

        User user = userRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        if (currentUser.getId() == user.getId()) throw new ApiException(ErrorCode.CANNOT_MODIFY_YOURSELF);
        if (user.getRole() == UserRole.ADMIN) throw new ApiException(ErrorCode.CANNOT_MODIFY_OTHER_ADMIN);

        user.setRole(request.getRole());

        return userMapper.toUserResponse(userRepository.save(user));
    }
}
