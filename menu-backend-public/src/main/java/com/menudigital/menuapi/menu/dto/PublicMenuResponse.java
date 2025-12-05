package com.menudigital.menuapi.menu.dto;

import com.menudigital.menuapi.menu.domain.Menu;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record PublicMenuResponse(
        UUID id,
        String name,
        boolean active,
        UUID companyId,
        Instant createdAt,
        Instant updatedAt,
        List<PublicMenuCategoryResponse> categories
) {
    public static PublicMenuResponse from(Menu menu, List<PublicMenuCategoryResponse> categories) {
        return new PublicMenuResponse(
                menu.getId(),
                menu.getName(),
                menu.isActive(),
                menu.getCompany().getId(),
                menu.getCreatedAt(),
                menu.getUpdatedAt(),
                categories
        );
    }
}
