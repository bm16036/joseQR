package com.menudigital.menuapi.menu.repo;

import com.menudigital.menuapi.menu.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByCategoryId(UUID categoryId);
    List<Product> findByCompanyId(UUID companyId);
    List<Product> findByMenusId(UUID menuId);
}
