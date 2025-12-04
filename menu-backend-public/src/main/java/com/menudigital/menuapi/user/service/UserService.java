package com.menudigital.menuapi.user.service;

import com.menudigital.menuapi.security.UserEntity;
import com.menudigital.menuapi.user.dto.UserRequest;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserEntity> list(UUID companyId);
    UserEntity get(UUID id);
    UserEntity create(UserRequest request);
    UserEntity update(UUID id, UserRequest request);
    void delete(UUID id);
}
