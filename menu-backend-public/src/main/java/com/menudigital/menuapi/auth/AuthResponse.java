package com.menudigital.menuapi.auth;

import com.menudigital.menuapi.security.UserEntity;
import com.menudigital.menuapi.user.dto.UserResponse;

/**
 * Respuesta estandarizada para el inicio de sesión.
 */
public record AuthResponse(String token, UserResponse user) {

    public static AuthResponse from(String token, UserEntity entity) {
        return new AuthResponse(token, UserResponse.from(entity));
    }
}
