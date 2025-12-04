package com.menudigital.menuapi.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    // Nuevo método para buscar por email
    Optional<UserEntity> findByEmail(String email);
}

