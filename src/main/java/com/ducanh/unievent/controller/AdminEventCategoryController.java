package com.ducanh.unievent.controller;

import com.ducanh.unievent.common.ApiResponse;
import com.ducanh.unievent.dto.request.CreateEventCategoryRequest;
import com.ducanh.unievent.dto.request.UpdateEventCategoryRequest;
import com.ducanh.unievent.dto.response.EventCategoryResponse;
import com.ducanh.unievent.service.EventCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/admin/categories")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminEventCategoryController {
    private final EventCategoryService eventCategoryService;

    @PostMapping()
    ResponseEntity<ApiResponse<EventCategoryResponse>> createEventCategory(@Valid @RequestBody CreateEventCategoryRequest request)
    {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(eventCategoryService.createEventCategory(request)));
    }

    @PatchMapping("/{eventCategoryId}")
    ResponseEntity<ApiResponse<EventCategoryResponse>> createEventCategory(
            @PathVariable Long eventCategoryId,
            @Valid @RequestBody UpdateEventCategoryRequest request)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(eventCategoryService.updateEventCategory(eventCategoryId, request)));
    }

    @DeleteMapping("/{eventCategoryId}")
    ResponseEntity<ApiResponse<EventCategoryResponse>> createEventCategory(@PathVariable Long eventCategoryId)
    {
        eventCategoryService.deleteEventCategory(eventCategoryId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(null));
    }


}
