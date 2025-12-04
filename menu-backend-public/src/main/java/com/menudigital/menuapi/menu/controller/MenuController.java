package com.menudigital.menuapi.menu.controller;

import com.menudigital.menuapi.menu.dto.MenuRequest;
import com.menudigital.menuapi.menu.dto.MenuResponse;
import com.menudigital.menuapi.menu.service.CompanyService;
import com.menudigital.menuapi.menu.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;
    private final CompanyService companyService;

    @GetMapping
    public List<MenuResponse> list(@RequestParam(required = false) UUID companyId) {
        return menuService.list(companyId).stream().map(MenuResponse::from).toList();
    }

    @GetMapping("/{id}")
    public MenuResponse get(@PathVariable UUID id) {
        return MenuResponse.from(menuService.get(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MenuResponse create(@Valid @RequestBody MenuRequest request) {
        var company = companyService.get(request.companyId());
        var menu = request.toEntity(company);
        return MenuResponse.from(menuService.create(menu));
    }

    @PutMapping("/{id}")
    public MenuResponse update(@PathVariable UUID id, @Valid @RequestBody MenuRequest request) {
        var company = companyService.get(request.companyId());
        var menu = request.toEntity(company);
        return MenuResponse.from(menuService.update(id, menu));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        menuService.delete(id);
    }
}
