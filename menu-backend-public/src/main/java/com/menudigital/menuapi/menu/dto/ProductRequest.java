package com.menudigital.menuapi.menu.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Información requerida para crear o actualizar un producto.
 */
public record ProductRequest(
        @NotBlank @Size(max = 120) String name,
        @NotBlank @Size(max = 800) String description,
        @NotNull @DecimalMin(value = "0.01") BigDecimal price,
        @NotBlank @Pattern(regexp = "^(https?://).+", message = "Debe ser una URL válida") String imageUrl,
        boolean active,
        @NotNull UUID categoryId,
        @NotNull UUID companyId,
        @NotEmpty List<UUID> menuIds
) {
}
