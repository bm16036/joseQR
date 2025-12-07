package com.menudigital.menuapi.menu.service;

import com.menudigital.menuapi.menu.domain.Menu;

import java.util.List;
import java.util.UUID;

public interface MenuService {
    List<Menu> list(UUID companyId);
    List<Menu> listActive(UUID companyId);
    Menu get(UUID id);
    Menu create(Menu menu);
    Menu update(UUID id, Menu menu);
    void delete(UUID id);
}
