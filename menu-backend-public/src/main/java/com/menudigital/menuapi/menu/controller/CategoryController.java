package com.menudigital.menuapi.menu.controller;

import com.menudigital.menuapi.menu.dto.CategoryRequest;
import com.menudigital.menuapi.menu.dto.CategoryResponse;
import com.menudigital.menuapi.menu.service.CategoryService;
import com.menudigital.menuapi.menu.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CompanyService companyService;

    @GetMapping
    public List<CategoryResponse> list(@RequestParam(required = false) UUID companyId) {
        return categoryService.list(companyId).stream().map(CategoryResponse::from).toList();
    }

    @GetMapping("/{id}")
    public CategoryResponse get(@PathVariable UUID id) {
        return CategoryResponse.from(categoryService.get(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse create(@Valid @RequestBody CategoryRequest request) {
        var company = companyService.get(request.companyId());
        var category = request.toEntity(company);
        return CategoryResponse.from(categoryService.create(category));
    }

    @PutMapping("/{id}")
    public CategoryResponse update(@PathVariable UUID id, @Valid @RequestBody CategoryRequest request) {
        var company = companyService.get(request.companyId());
        var category = request.toEntity(company);
        return CategoryResponse.from(categoryService.update(id, category));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        categoryService.delete(id);
    }
}
