package com.ducanh.unievent.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateEventRequest {
    @NotBlank(message = "Title must not be blank")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;

    @Size(max = 450, message = "Description must not exceed 450 characters")
    private String description;

    @NotBlank(message = "Location must not be blank")
    @Size(max = 450, message = "Location must not exceed 450 characters")
    private String location;

    @NotNull(message = "Start time must not be null")
    @Future(message = "Start time must be in the future")
    private Instant startTime;

    @NotNull(message = "End time must not be null")
    @Future(message = "End time must be in the future")
    private Instant endTime;

    @NotNull(message = "Registration deadline time must not be null")
    @Future(message = "Registration deadline must be in the future")
    private Instant registrationDeadline;

    @NotNull(message = "Max participants must not be null")
    @Min(value = 1, message = "Max participants must be greater than 0")
    private Integer maxParticipants;

    @NotNull(message = "Category id must not be null")
    private Long categoryId;
}
