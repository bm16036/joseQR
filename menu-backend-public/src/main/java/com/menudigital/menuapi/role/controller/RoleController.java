package com.menudigital.menuapi.role.controller;

import com.menudigital.menuapi.role.dto.RoleResponse;
import com.menudigital.menuapi.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public List<RoleResponse> list() {
        return roleService.list().stream()
                .map(RoleResponse::from)
                .toList();
    }
}
