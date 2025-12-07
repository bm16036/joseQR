package com.menudigital.menuapi.menu.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.menudigital.menuapi.menu.domain.Category;
import com.menudigital.menuapi.menu.domain.Company;
import com.menudigital.menuapi.menu.domain.Menu;
import com.menudigital.menuapi.menu.dto.ProductRequest;
import com.menudigital.menuapi.menu.repo.CategoryRepository;
import com.menudigital.menuapi.menu.repo.CompanyRepository;
import com.menudigital.menuapi.menu.repo.MenuRepository;
import com.menudigital.menuapi.menu.repo.ProductRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductServiceImplTest {

  @Autowired private ProductService productService;
  @Autowired private MenuRepository menuRepository;
  @Autowired private CategoryRepository categoryRepository;
  @Autowired private CompanyRepository companyRepository;
  @Autowired private ProductRepository productRepository;
  @Autowired private EntityManager entityManager;

  @BeforeEach
  void cleanDatabase() {
    productRepository.deleteAll();
    menuRepository.deleteAll();
    categoryRepository.deleteAll();
    companyRepository.deleteAll();
  }

  @Test
  void updateShouldFlushMenuAssociationsBeforeReattaching() {
    var company =
        companyRepository.save(
            Company.builder()
                .businessName("Test Company")
                .commercialName("Test Company")
                .email("test@example.com")
                .phone("123456789")
                .build());

    var category = categoryRepository.save(Category.builder().name("Bebidas").company(company).build());
    var menuA = menuRepository.save(Menu.builder().name("Menú A").company(company).build());
    var menuB = menuRepository.save(Menu.builder().name("Menú B").company(company).build());

    var createRequest =
        new ProductRequest(
            "Soda",
            "Bebida fría",
            new BigDecimal("4.50"),
            "http://example.com/soda.jpg",
            true,
            category.getId(),
            company.getId(),
            List.of(menuA.getId()));

    var product = productService.create(createRequest);

    var updateRequest =
        new ProductRequest(
            "Soda",
            "Bebida fría",
            new BigDecimal("4.50"),
            "http://example.com/soda.jpg",
            true,
            category.getId(),
            company.getId(),
            List.of(menuA.getId(), menuB.getId(), menuA.getId()));

    var updated = productService.update(product.getId(), updateRequest);

    entityManager.flush();
    entityManager.clear();

    var persisted = productRepository.findById(updated.getId()).orElseThrow();
    assertThat(persisted.getMenus())
        .extracting(Menu::getId)
        .containsExactlyInAnyOrder(menuA.getId(), menuB.getId());
  }
}
