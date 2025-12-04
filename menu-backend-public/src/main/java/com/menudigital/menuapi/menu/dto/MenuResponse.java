package com.menudigital.menuapi.menu.dto;

import com.menudigital.menuapi.menu.domain.Menu;

import java.time.Instant;
import java.util.UUID;

public record MenuResponse(
        UUID id,
        String name,
        boolean active,
        UUID companyId,
        Instant createdAt,
        Instant updatedAt
) {
    public static MenuResponse from(Menu menu) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.isActive(),
                menu.getCompany().getId(),
                menu.getCreatedAt(),
                menu.getUpdatedAt()
        );
    }
}
