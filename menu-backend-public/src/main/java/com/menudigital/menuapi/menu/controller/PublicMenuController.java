package com.menudigital.menuapi.menu.controller;

import com.menudigital.menuapi.menu.dto.CategoryResponse;
import com.menudigital.menuapi.menu.dto.MenuResponse;
import com.menudigital.menuapi.menu.dto.ProductResponse;
import com.menudigital.menuapi.menu.service.CategoryService;
import com.menudigital.menuapi.menu.service.MenuPdfService;
import com.menudigital.menuapi.menu.service.MenuService;
import com.menudigital.menuapi.menu.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    private final MenuPdfService menuPdfService;

    @GetMapping("/companies/{companyId}/categories")
    public List<CategoryResponse> categories(@PathVariable UUID companyId) {
        return categoryService.list(companyId).stream().map(CategoryResponse::from).toList();
    }

    @GetMapping("/companies/{companyId}/menu")
    public List<MenuResponse> menu(@PathVariable UUID companyId) {
        return menuService.listActive(companyId).stream().map(MenuResponse::from).toList();
    }

    @GetMapping("/categories/{categoryId}/products")
    public List<ProductResponse> productsByCategory(@PathVariable UUID categoryId) {
        return productService.list(null, categoryId, null).stream().map(ProductResponse::from).toList();
    }

    @GetMapping("/companies/{companyId}/products")
    public List<ProductResponse> productsByCompany(@PathVariable UUID companyId) {
        return productService.list(companyId, null, null).stream().map(ProductResponse::from).toList();
    }

    @GetMapping(value = "/companies/{companyId}/menu.pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> menuPdf(@PathVariable UUID companyId) {
        byte[] pdf = menuPdfService.generateCompanyMenuPdf(companyId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=menu-" + companyId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
