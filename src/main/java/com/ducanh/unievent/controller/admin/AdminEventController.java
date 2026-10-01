package com.ducanh.unievent.controller.admin;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.ducanh.unievent.common.ApiResponse;
import com.ducanh.unievent.common.PageResponse;
import com.ducanh.unievent.dto.request.EventFilterRequest;
import com.ducanh.unievent.dto.response.EventResponse;
import com.ducanh.unievent.service.EventService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1/admin/events")
@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminEventController {
    private final EventService eventService;

    @GetMapping("/{eventId}")
    public ResponseEntity<ApiResponse<EventResponse>> getEvent(@PathVariable Long eventId) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(eventService.getAdminEvent(eventId)));
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<PageResponse<EventResponse>>> getEvents(
            @ModelAttribute @Valid EventFilterRequest filter,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "startTime,asc") String sort) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        PageResponse.<EventResponse>from(eventService.getAdminEvents(filter, page, size, sort))));
    }

    @PostMapping("/{eventId}/cancel")
    public ResponseEntity<ApiResponse<EventResponse>> cancelEvent(@PathVariable Long eventId) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(eventService.cancelAdminEvent(eventId)));
    }

    @PostMapping("/{eventId}/approve")
    public ResponseEntity<ApiResponse<EventResponse>> approveEvent(@PathVariable Long eventId) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(eventService.approveEvent(eventId)));
    }

    @PostMapping("/{eventId}/reject")
    public ResponseEntity<ApiResponse<EventResponse>> rejectEvent(@PathVariable Long eventId) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(eventService.rejectEvent(eventId)));
    }
}
