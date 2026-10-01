package com.ducanh.unievent.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.ducanh.unievent.common.ApiResponse;
import com.ducanh.unievent.common.PageResponse;
import com.ducanh.unievent.dto.request.EventFilterRequest;
import com.ducanh.unievent.dto.response.EventResponse;
import com.ducanh.unievent.service.EventService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1/events")
@Controller
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping("/{eventId}")
    public ResponseEntity<ApiResponse<EventResponse>> getPublicEvent(@PathVariable Long eventId) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(eventService.getPublicEvent(eventId)));
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<PageResponse<EventResponse>>> getPublicEvents(
            @ModelAttribute @Valid EventFilterRequest filter,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "startTime,asc") String sort) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        PageResponse.<EventResponse>from(eventService.getPublicEvents(filter, page, size, sort))));
    }
}
