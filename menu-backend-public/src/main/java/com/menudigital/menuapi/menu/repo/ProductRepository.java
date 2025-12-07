package com.menudigital.menuapi.menu.repo;

import com.menudigital.menuapi.menu.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByCategoryId(UUID categoryId);
    List<Product> findByCompanyId(UUID companyId);
    List<Product> findByMenusId(UUID menuId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "DELETE FROM menu_products WHERE product_id = :productId", nativeQuery = true)
    void deleteMenus(UUID productId);
}
