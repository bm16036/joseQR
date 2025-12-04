package com.menudigital.menuapi.menu.service.impl;

import com.menudigital.menuapi.menu.domain.Category;
import com.menudigital.menuapi.menu.repo.CategoryRepository;
import com.menudigital.menuapi.menu.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Category> list(UUID companyId) {
        if (companyId != null) {
            return repository.findByCompanyIdOrderByNameAsc(companyId);
        }
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Category get(UUID id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    @Transactional
    public Category create(Category category) {
        return repository.save(category);
    }

    @Override
    @Transactional
    public Category update(UUID id, Category category) {
        var existing = get(id);
        existing.setName(category.getName());
        existing.setActive(category.isActive());
        existing.setCompany(category.getCompany());
        return repository.save(existing);
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
