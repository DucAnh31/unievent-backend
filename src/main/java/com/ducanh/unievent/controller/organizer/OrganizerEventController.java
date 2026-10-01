package com.ducanh.unievent.controller.organizer;

import com.ducanh.unievent.common.ApiResponse;
import com.ducanh.unievent.common.PageResponse;
import com.ducanh.unievent.dto.request.*;
import com.ducanh.unievent.dto.response.*;
import com.ducanh.unievent.service.CheckInService;
import com.ducanh.unievent.service.EventRegistrationService;
import com.ducanh.unievent.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
    private final EventRegistrationService registrationService;
    private final CheckInService checkInService;

    @PostMapping
    public ResponseEntity<ApiResponse<EventResponse>> createEvent(@Valid @RequestBody CreateEventRequest request)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(eventService.createEvent(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<EventResponse>>> getMyEvents(
            @Valid @ModelAttribute EventFilterRequest filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startTime,asc") String sort
    )
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(PageResponse.from(eventService.getMyEvents(filter, page, size, sort))));
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
    @PostMapping("/{eventId}/submit-for-approval")
    public ResponseEntity<ApiResponse<EventResponse>> submitEvent(@PathVariable Long eventId)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(eventService.submitEventForApproval(eventId)));
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

    @GetMapping("/{eventId}/registrations")
    public ResponseEntity<ApiResponse<PageResponse<EventRegistrationResponse>>> getEventRegistrations(
            @PathVariable Long eventId,
            @Valid @ModelAttribute RegistrationFilterRequest filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "registeredAt,desc") String sort)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        PageResponse.from(registrationService.getEventRegistrations(eventId, filter, page, size, sort))));
    }

    @DeleteMapping("/{eventId}/registrations/{registrationId}")
    public ResponseEntity<ApiResponse<Void>> cancelEventRegistrations(
            @PathVariable Long eventId,
            @PathVariable Long registrationId)
    {
        registrationService.cancelRegistrationByOrganizer(eventId, registrationId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(null));
    }

    @PostMapping("/{eventId}/check-ins")
    public ResponseEntity<ApiResponse<CheckInResponse>> checkIn(@PathVariable Long eventId, @Valid @RequestBody CheckInRequest request)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(checkInService.checkIn(eventId, request)));
    }

    @GetMapping("/{eventId}/check-ins")
    public ResponseEntity<ApiResponse<PageResponse<CheckInResponse>>> getEventCheckIns(
            @PathVariable Long eventId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "checkInTime,desc") String sort)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(PageResponse
                        .from(checkInService.getEventCheckIns(eventId, page, size, sort))));
    }

    //dashboard
    @GetMapping("/{eventId}/statistics")
    public ResponseEntity<ApiResponse<EventStatisticsResponse>> getEventStatistics(@PathVariable Long eventId)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(eventService.getMyEventStatistics(eventId)));
    }


}
