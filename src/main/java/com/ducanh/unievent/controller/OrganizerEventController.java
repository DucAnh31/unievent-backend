package com.ducanh.unievent.controller;

import com.ducanh.unievent.common.ApiResponse;
import com.ducanh.unievent.dto.request.CreateEventRequest;
import com.ducanh.unievent.dto.request.UpdateEventRequest;
import com.ducanh.unievent.dto.response.EventResponse;
import com.ducanh.unievent.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/organizer/events")
@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ORGANIZER')")
public class OrganizerEventController {
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<ApiResponse<EventResponse>> createEvent(@Valid @RequestBody CreateEventRequest request)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(eventService.createEvent(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EventResponse>>> getMyEvents()
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(eventService.getMyEvents()));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<ApiResponse<EventResponse>> getMyEvent(@PathVariable Long eventId)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(eventService.getMyEvent(eventId)));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<ApiResponse<EventResponse>> updateMyEvent(@PathVariable Long eventId,@Valid @RequestBody UpdateEventRequest request)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(eventService.updateMyEvent(eventId, request)));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<ApiResponse<Void>> deleteMyEvent(@PathVariable Long eventId)
    {
        eventService.deleteMyEvent(eventId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(null));
    }
    @PostMapping("/{eventId}/publish")
    public ResponseEntity<ApiResponse<EventResponse>> publishEvent(@PathVariable Long eventId)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(eventService.publishEvent(eventId)));
    }
    @PostMapping("/{eventId}/close-registration")
    public ResponseEntity<ApiResponse<EventResponse>> closeRegistration(@PathVariable Long eventId)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(eventService.closeRegistration(eventId)));
    }
    @PostMapping("/{eventId}/cancel")
    public ResponseEntity<ApiResponse<EventResponse>> cancelEvent(@PathVariable Long eventId)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(eventService.cancelEvent(eventId)));
    }


}
