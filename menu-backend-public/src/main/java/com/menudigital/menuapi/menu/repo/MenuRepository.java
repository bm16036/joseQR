package com.menudigital.menuapi.menu.repo;

import com.menudigital.menuapi.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MenuRepository extends JpaRepository<Menu, UUID> {

    List<Menu> findByCompanyIdOrderByNameAsc(UUID companyId);

    List<Menu> findByCompanyIdAndActiveTrueOrderByNameAsc(UUID companyId);
}
