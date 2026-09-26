package com.ducanh.unievent.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateEventCategoryRequest {
    @NotBlank(message = "Name must not be blank")
    @Size(max = 200, message = "Name  must not exceed 200 characters")
    private String name;

    @Size(max = 450, message = "Description must not exceed 450 characters")
    private String description;
}
