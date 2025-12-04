package com.menudigital.menuapi.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * Datos utilizados para crear o actualizar usuarios internos.
 */
public record UserRequest(
        @NotBlank @Size(max = 80) String username,
        @NotBlank @Email @Size(max = 120) String email,
        @NotBlank String role,
        @NotNull UUID companyId,
        @NotNull Boolean active,
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres") String password
) {
}
