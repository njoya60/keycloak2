package com.mobapp.j2ds.mobappsecurity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Builder
public record UserDto(
        @NotBlank(message = "Veuillez indiquer un username")
        @NotNull(message = "Veuillez indiquer un username")
        String username,
        @NotBlank(message = "Veuillez indiquer une adresse email")
        @Email(message = "Veuillez indiquer un email valide")
        String email,
        String firstName,
        String lastName,
        @NotBlank(message = "Veuillez indiquer un mot de passe")
        String password,
        Set<String> roles,
        Map<String, List<String>> attributes
        ) {
}
