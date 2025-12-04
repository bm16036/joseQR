package com.menudigital.menuapi.role.dto;

import com.menudigital.menuapi.security.Role;

import java.util.UUID;

public record RoleResponse(
        UUID id,
        String nombre,
        String descripcion
) {
    public static RoleResponse from(Role role) {
        return new RoleResponse(
                role.getId(),
                role.getNombre(),
                role.getDescripcion()
        );
    }
}
