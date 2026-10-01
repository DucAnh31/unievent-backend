package com.ducanh.unievent.controller.student;

import com.ducanh.unievent.common.ApiResponse;
import com.ducanh.unievent.common.PageResponse;
import com.ducanh.unievent.dto.response.CheckInCodeResponse;
import com.ducanh.unievent.dto.response.EventRegistrationResponse;
import com.ducanh.unievent.dto.response.StudentCheckInStatusResponse;
import com.ducanh.unievent.service.CheckInService;
import com.ducanh.unievent.service.EventRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/student")
@PreAuthorize("hasRole('STUDENT')")
public class StudentEventRegistrationController {
    private final EventRegistrationService registrationEventService;
    private final CheckInService checkInService;

    @PostMapping("/events/{eventId}/registrations")
    public ResponseEntity<ApiResponse<EventRegistrationResponse>> register(@PathVariable Long eventId)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(registrationEventService.registerEvent(eventId)));
    }

    @DeleteMapping("/events/{eventId}/registrations")
    public ResponseEntity<ApiResponse<Void>> cancel(@PathVariable Long eventId)
    {
        registrationEventService.cancelRegistration(eventId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(null));
    }

    @GetMapping("registrations/{registrationId}")
    public ResponseEntity<ApiResponse<EventRegistrationResponse>> getMyRegistration(@PathVariable Long registrationId)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(registrationEventService.getMyRegistration(registrationId)));
    }

    @GetMapping("registrations")
    public ResponseEntity<ApiResponse<PageResponse<EventRegistrationResponse>>> getMyRegistrations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "registeredAt,desc") String sort)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(PageResponse.from(registrationEventService.getMyRegistrations(page, size, sort))));
    }

    @GetMapping("registrations/{registrationId}/check-in-code")
    public ResponseEntity<ApiResponse<CheckInCodeResponse>> getMyCheckInCode(@PathVariable Long registrationId)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(registrationEventService.getMyCheckInCode(registrationId)));
    }
    @GetMapping("/registrations/{registrationId}/check-in")
    public ResponseEntity<ApiResponse<StudentCheckInStatusResponse>> getMyCheckInStatus(@PathVariable Long registrationId)
    {
        System.out.println("hello");
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(checkInService.getMyCheckInStatus(registrationId)));
    }

}
