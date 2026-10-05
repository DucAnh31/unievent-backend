package com.ducanh.unievent.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.ducanh.unievent.common.ApiResponse;
import com.ducanh.unievent.common.PageResponse;
import com.ducanh.unievent.dto.response.EventCategoryResponse;
import com.ducanh.unievent.service.EventCategoryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class EventCategoryController {
    private final EventCategoryService eventCategoryService;

    @GetMapping("/{eventCategoryId}")
    ResponseEntity<ApiResponse<EventCategoryResponse>> getEventCategory(@PathVariable Long eventCategoryId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(eventCategoryService.getEventCategory(eventCategoryId)));
    }

    @GetMapping()
    ResponseEntity<ApiResponse<PageResponse<EventCategoryResponse>>> getEventCategories(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name,asc") String sort) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        PageResponse.from(eventCategoryService.getEventCategories(page, size, sort, keyword))));
    }
}
