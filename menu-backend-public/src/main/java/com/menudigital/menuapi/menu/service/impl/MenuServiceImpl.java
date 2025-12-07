package com.menudigital.menuapi.menu.service.impl;

import com.menudigital.menuapi.menu.domain.Menu;
import com.menudigital.menuapi.menu.repo.MenuRepository;
import com.menudigital.menuapi.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Menu> list(UUID companyId) {
        if (companyId != null) {
            return repository.findByCompanyIdOrderByNameAsc(companyId);
        }
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Menu> listActive(UUID companyId) {
        if (companyId != null) {
            return repository.findByCompanyIdAndActiveTrueOrderByNameAsc(companyId);
        }
        return repository.findAll().stream().filter(Menu::isActive).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Menu get(UUID id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    @Transactional
    public Menu create(Menu menu) {
        return repository.save(menu);
    }

    @Override
    @Transactional
    public Menu update(UUID id, Menu menu) {
        var existing = get(id);
        existing.setName(menu.getName());
        existing.setActive(menu.isActive());
        existing.setCompany(menu.getCompany());
        return repository.save(existing);
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
