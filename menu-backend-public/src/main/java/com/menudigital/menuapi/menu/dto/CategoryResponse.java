package com.menudigital.menuapi.menu.dto;

import com.menudigital.menuapi.menu.domain.Category;

import java.time.Instant;
import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name,
        boolean active,
        UUID companyId,
        Instant createdAt,
        Instant updatedAt
) {
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.isActive(),
                category.getCompany().getId(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}
