package com.menudigital.menuapi.menu.dto;

import com.menudigital.menuapi.menu.domain.Company;
import com.menudigital.menuapi.menu.domain.Menu;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Datos recibidos para crear o modificar menús.
 */
public record MenuRequest(
        @NotBlank @Size(max = 120) String name,
        @NotNull Boolean active,
        @NotNull java.util.UUID companyId
) {
    public Menu toEntity(Company company) {
        return Menu.builder()
                .name(name)
                .active(Boolean.TRUE.equals(active))
                .company(company)
                .build();
    }
}
