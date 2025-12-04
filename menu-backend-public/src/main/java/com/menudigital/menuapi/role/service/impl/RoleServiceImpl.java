package com.menudigital.menuapi.role.service.impl;

import com.menudigital.menuapi.role.service.RoleService;
import com.menudigital.menuapi.security.Role;
import com.menudigital.menuapi.security.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Role> list() {
        return roleRepository.findAll();
    }
}
