package com.menudigital.menuapi.menu.controller;

import com.menudigital.menuapi.menu.dto.CategoryResponse;
import com.menudigital.menuapi.menu.dto.MenuResponse;
import com.menudigital.menuapi.menu.dto.ProductResponse;
import com.menudigital.menuapi.menu.service.CategoryService;
import com.menudigital.menuapi.menu.service.MenuService;
import com.menudigital.menuapi.menu.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicMenuController {

    private final CategoryService categoryService;
    private final MenuService menuService;
    private final ProductService productService;

    @GetMapping("/companies/{companyId}/categories")
    public List<CategoryResponse> categories(@PathVariable UUID companyId) {
        return categoryService.list(companyId).stream().map(CategoryResponse::from).toList();
    }

    @GetMapping("/companies/{companyId}/menu")
    public List<MenuResponse> menu(@PathVariable UUID companyId) {
        return menuService.list(companyId).stream().map(MenuResponse::from).toList();
    }

    @GetMapping("/categories/{categoryId}/products")
    public List<ProductResponse> productsByCategory(@PathVariable UUID categoryId) {
        return productService.list(null, categoryId, null).stream().map(ProductResponse::from).toList();
    }

    @GetMapping("/companies/{companyId}/products")
    public List<ProductResponse> productsByCompany(@PathVariable UUID companyId) {
        return productService.list(companyId, null, null).stream().map(ProductResponse::from).toList();
    }
}
