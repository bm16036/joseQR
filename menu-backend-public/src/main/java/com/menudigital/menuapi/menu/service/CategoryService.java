package com.menudigital.menuapi.menu.service;

import com.menudigital.menuapi.menu.domain.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<Category> list(UUID companyId);
    Category get(UUID id);
    Category create(Category category);
    Category update(UUID id, Category category);
    void delete(UUID id);
}
