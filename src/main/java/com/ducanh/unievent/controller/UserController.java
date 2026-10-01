package com.ducanh.unievent.controller;

import com.ducanh.unievent.common.ApiResponse;
import com.ducanh.unievent.dto.request.ChangePasswordRequest;
import com.ducanh.unievent.dto.request.UpdateUserRequest;
import com.ducanh.unievent.dto.response.UserResponse;
import com.ducanh.unievent.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyProfile()
    {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(userService.getMyProfile()));
    }

    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateMyProfile(@Valid @RequestBody UpdateUserRequest request)
    {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(userService.updateMyProfile(request)));
    }

    //chua test
    @PatchMapping("/me/password")
    public ResponseEntity<ApiResponse<Void>> changeMyPassword(@Valid @RequestBody ChangePasswordRequest request)
    {
        userService.changePassword(request);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(null));
    }
}
