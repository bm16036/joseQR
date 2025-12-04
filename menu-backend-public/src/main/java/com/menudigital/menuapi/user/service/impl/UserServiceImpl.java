package com.menudigital.menuapi.user.service.impl;

import com.menudigital.menuapi.menu.repo.CompanyRepository;
import com.menudigital.menuapi.security.RoleRepository;
import com.menudigital.menuapi.security.UserEntity;
import com.menudigital.menuapi.security.UserRepository;
import com.menudigital.menuapi.user.dto.UserRequest;
import com.menudigital.menuapi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<UserEntity> list(UUID companyId) {
        var users = userRepository.findAll();
        return users.stream()
                .filter(user -> companyId == null || user.getCompany().getId().equals(companyId))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity get(UUID id) {
        return userRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional
    public UserEntity create(UserRequest request) {
        if (request.password() == null || request.password().isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        var role = roleRepository.findByNombre(request.role()).orElseThrow();
        var company = companyRepository.findById(request.companyId()).orElseThrow();

        var user = UserEntity.builder()
                .username(request.username())
                .email(request.email())
                .role(role)
                .company(company)
                .active(Boolean.TRUE.equals(request.active()))
                .passwordHash(passwordEncoder.encode(request.password()))
                .build();

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public UserEntity update(UUID id, UserRequest request) {
        var user = get(id);
        var role = roleRepository.findByNombre(request.role()).orElseThrow();
        var company = companyRepository.findById(request.companyId()).orElseThrow();

        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setRole(role);
        user.setCompany(company);
        user.setActive(Boolean.TRUE.equals(request.active()));

        if (request.password() != null && !request.password().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(request.password()));
        }

        return userRepository.save(user);
    }

    @Override
    public void delete(UUID id) {
        userRepository.deleteById(id);
    }
}
