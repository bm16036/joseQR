package com.menudigital.menuapi.menu.dto;

import com.menudigital.menuapi.menu.domain.Menu;
import com.menudigital.menuapi.menu.domain.Product;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        String imageUrl,
        boolean active,
        UUID categoryId,
        List<UUID> menuIds,
        UUID companyId,
        Instant createdAt,
        Instant updatedAt
) {
    public static ProductResponse from(Product product) {
        var menuIds = product.getMenus().stream()
                .map(Menu::getId)
                .toList();

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl(),
                product.isActive(),
                product.getCategory().getId(),
                menuIds,
                product.getCompany().getId(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
