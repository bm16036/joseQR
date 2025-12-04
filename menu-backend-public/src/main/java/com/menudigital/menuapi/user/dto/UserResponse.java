package com.menudigital.menuapi.user.dto;

import com.menudigital.menuapi.security.UserEntity;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String email,
        String role,
        UUID companyId,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {
    public static UserResponse from(UserEntity entity) {
        return new UserResponse(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getRole().getNombre(),
                entity.getCompany().getId(),
                entity.isActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
