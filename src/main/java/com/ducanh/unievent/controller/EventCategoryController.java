package com.ducanh.unievent.controller;

import com.ducanh.unievent.common.ApiResponse;
import com.ducanh.unievent.dto.response.EventCategoryResponse;
import com.ducanh.unievent.service.EventCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class EventCategoryController {
    private final EventCategoryService eventCategoryService;

    @GetMapping("/{eventCategoryId}")
    ResponseEntity<ApiResponse<EventCategoryResponse>> getEventCategory(@PathVariable Long eventCategoryId)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(eventCategoryService.getEventCategory(eventCategoryId)));
    }

    @GetMapping()
    ResponseEntity<ApiResponse<List<EventCategoryResponse>>> getEventCategories()
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(eventCategoryService.getEventCategories()));
    }




}
