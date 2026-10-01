package com.ducanh.unievent.controller.organizer;

import com.ducanh.unievent.common.ApiResponse;
import com.ducanh.unievent.dto.request.CheckInRequest;
import com.ducanh.unievent.dto.request.CreateEventRequest;
import com.ducanh.unievent.dto.request.UpdateEventRequest;
import com.ducanh.unievent.dto.response.CheckInResponse;
import com.ducanh.unievent.dto.response.EventResponse;
import com.ducanh.unievent.dto.response.EventStatisticsResponse;
import com.ducanh.unievent.dto.response.OrganizerRegistrationResponse;
import com.ducanh.unievent.service.CheckInService;
import com.ducanh.unievent.service.EventRegistrationService;
import com.ducanh.unievent.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/{eventId}/registrations")
    public ResponseEntity<ApiResponse<List<OrganizerRegistrationResponse>>> getEventRegistrations(@PathVariable Long eventId)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(registrationService.getEventRegistrations(eventId)));
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
    public ResponseEntity<ApiResponse<List<CheckInResponse>>> getEventCheckIns(@PathVariable Long eventId)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(checkInService.getEventCheckIns(eventId)));
    }

    //dashboard
    @GetMapping("/{eventId}/statistics")
    public ResponseEntity<ApiResponse<EventStatisticsResponse>> getEventStatistics(@PathVariable Long eventId)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(eventService.getMyEventStatistics(eventId)));
    }


}
