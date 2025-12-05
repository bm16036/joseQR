package com.menudigital.menuapi.menu.repo;

import com.menudigital.menuapi.menu.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByCategoryId(UUID categoryId);
    List<Product> findByCompanyId(UUID companyId);
    List<Product> findByMenusId(UUID menuId);

    @Query("select distinct p from Product p " +
            "join fetch p.category c " +
            "join fetch p.menus m " +
            "where p.company.id = :companyId")
    List<Product> findByCompanyIdWithMenus(UUID companyId);
}
