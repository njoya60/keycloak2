package com.mobapp.j2ds.mobappsecurity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GroupDto(
        @NotBlank(message = "Veuillez indiquer un group")
        @NotNull
        String name) {
}
