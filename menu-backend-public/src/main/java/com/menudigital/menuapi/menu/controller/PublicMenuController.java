package com.menudigital.menuapi.menu.controller;

import com.menudigital.menuapi.menu.domain.Category;
import com.menudigital.menuapi.menu.domain.Menu;
import com.menudigital.menuapi.menu.domain.Product;
import com.menudigital.menuapi.menu.dto.CategoryResponse;
import com.menudigital.menuapi.menu.dto.MenuResponse;
import com.menudigital.menuapi.menu.dto.ProductResponse;
import com.menudigital.menuapi.menu.dto.PublicMenuCategoryResponse;
import com.menudigital.menuapi.menu.dto.PublicMenuResponse;
import com.menudigital.menuapi.menu.service.CategoryService;
import com.menudigital.menuapi.menu.service.MenuService;
import com.menudigital.menuapi.menu.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @GetMapping("/companies/{companyId}/menu/hierarchy")
    public List<PublicMenuResponse> menuHierarchy(@PathVariable UUID companyId) {
        var categories = categoryService.list(companyId);
        var menus = menuService.list(companyId);
        var productsByMenu = productService.listGroupedByMenuAndCategory(companyId);

        var categoriesById = categories.stream()
                .collect(Collectors.toMap(Category::getId, Function.identity()));

        return menus.stream()
                .sorted(Comparator.comparing(Menu::getName, String.CASE_INSENSITIVE_ORDER))
                .map(menu -> buildMenuResponse(menu, productsByMenu.get(menu.getId()), categoriesById))
                .filter(Objects::nonNull)
                .toList();
    }

    private PublicMenuResponse buildMenuResponse(Menu menu,
                                                Map<UUID, List<Product>> productsByCategory,
                                                Map<UUID, Category> categoriesById) {
        if (productsByCategory == null || productsByCategory.isEmpty()) {
            return null;
        }

        var categories = productsByCategory.entrySet().stream()
                .map(entry -> toCategoryResponse(entry.getKey(), entry.getValue(), categoriesById))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(PublicMenuCategoryResponse::name, String.CASE_INSENSITIVE_ORDER))
                .toList();

        if (categories.isEmpty()) {
            return null;
        }

        return PublicMenuResponse.from(menu, categories);
    }

    private PublicMenuCategoryResponse toCategoryResponse(UUID categoryId,
                                                          List<Product> products,
                                                          Map<UUID, Category> categoriesById) {
        var category = categoriesById.get(categoryId);

        if (category == null) {
            return null;
        }

        var productResponses = products.stream()
                .sorted(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER))
                .map(ProductResponse::from)
                .toList();

        return PublicMenuCategoryResponse.from(category, productResponses);
    }
}
