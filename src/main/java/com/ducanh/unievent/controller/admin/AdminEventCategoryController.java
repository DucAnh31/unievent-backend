package com.ducanh.unievent.controller.admin;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.ducanh.unievent.common.ApiResponse;
import com.ducanh.unievent.dto.request.CreateEventCategoryRequest;
import com.ducanh.unievent.dto.request.UpdateEventCategoryRequest;
import com.ducanh.unievent.dto.response.EventCategoryResponse;
import com.ducanh.unievent.service.EventCategoryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/v1/admin/categories")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminEventCategoryController {
    private final EventCategoryService eventCategoryService;

    @PostMapping()
    public ResponseEntity<ApiResponse<EventCategoryResponse>> createEventCategory(
            @Valid @RequestBody CreateEventCategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(eventCategoryService.createEventCategory(request)));
    }

    @PatchMapping("/{eventCategoryId}")
    public ResponseEntity<ApiResponse<EventCategoryResponse>> updateEventCategory(
            @PathVariable Long eventCategoryId, @Valid @RequestBody UpdateEventCategoryRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(eventCategoryService.updateEventCategory(eventCategoryId, request)));
    }

    @DeleteMapping("/{eventCategoryId}")
    public ResponseEntity<ApiResponse<EventCategoryResponse>> deleteEventCategory(@PathVariable Long eventCategoryId) {
        eventCategoryService.deleteEventCategory(eventCategoryId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(null));
    }
}
