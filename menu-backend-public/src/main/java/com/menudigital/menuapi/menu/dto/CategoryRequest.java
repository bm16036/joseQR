package com.menudigital.menuapi.menu.dto;

import com.menudigital.menuapi.menu.domain.Category;
import com.menudigital.menuapi.menu.domain.Company;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Payload para administrar categorías desde el panel.
 */
public record CategoryRequest(
        @NotBlank @Size(max = 120) String name,
        @NotNull Boolean active,
        @NotNull java.util.UUID companyId
) {
    public Category toEntity(Company company) {
        return Category.builder()
                .name(name)
                .active(Boolean.TRUE.equals(active))
                .company(company)
                .build();
    }
}
