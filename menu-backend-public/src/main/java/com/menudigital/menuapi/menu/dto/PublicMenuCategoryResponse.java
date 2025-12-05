package com.menudigital.menuapi.menu.dto;

import com.menudigital.menuapi.menu.domain.Category;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record PublicMenuCategoryResponse(
        UUID id,
        String name,
        boolean active,
        UUID companyId,
        Instant createdAt,
        Instant updatedAt,
        List<ProductResponse> products
) {
    public static PublicMenuCategoryResponse from(Category category, List<ProductResponse> products) {
        return new PublicMenuCategoryResponse(
                category.getId(),
                category.getName(),
                category.isActive(),
                category.getCompany().getId(),
                category.getCreatedAt(),
                category.getUpdatedAt(),
                products
        );
    }
}
