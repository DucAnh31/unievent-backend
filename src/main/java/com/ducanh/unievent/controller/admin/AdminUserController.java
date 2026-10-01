package com.ducanh.unievent.controller.admin;

import com.ducanh.unievent.common.ApiResponse;
import com.ducanh.unievent.dto.request.ChangeUserRoleRequest;
import com.ducanh.unievent.dto.request.ChangeUserStatusRequest;
import com.ducanh.unievent.dto.response.UserResponse;
import com.ducanh.unievent.service.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/admin/users")
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {
    private final AdminUserService adminUserService;

    @PatchMapping("/{id}/status")
    ResponseEntity<ApiResponse<UserResponse>>
        changeUserStatus(@PathVariable Long id, @Valid @RequestBody ChangeUserStatusRequest request)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(adminUserService.changeUserStatus(request, id)));
    }

    @PatchMapping("/{id}/role")
    ResponseEntity<ApiResponse<UserResponse>>
    changeUserRole(@PathVariable Long id,@Valid @RequestBody ChangeUserRoleRequest request)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(adminUserService.changeUserRole(request, id)));
    }
}
