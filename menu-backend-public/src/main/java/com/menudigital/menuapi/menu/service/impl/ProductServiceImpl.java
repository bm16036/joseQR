package com.menudigital.menuapi.menu.service.impl;

import com.menudigital.menuapi.menu.domain.Category;
import com.menudigital.menuapi.menu.domain.Company;
import com.menudigital.menuapi.menu.domain.Menu;
import com.menudigital.menuapi.menu.domain.Product;
import com.menudigital.menuapi.menu.dto.ProductRequest;
import com.menudigital.menuapi.menu.repo.CategoryRepository;
import com.menudigital.menuapi.menu.repo.CompanyRepository;
import com.menudigital.menuapi.menu.repo.MenuRepository;
import com.menudigital.menuapi.menu.repo.ProductRepository;
import com.menudigital.menuapi.menu.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CompanyRepository companyRepository;
    private final MenuRepository menuRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Product> list(UUID companyId, UUID categoryId, UUID menuId) {
        List<Product> products;

        if (menuId != null) {
            products = productRepository.findByMenusId(menuId);
        } else if (categoryId != null) {
            products = productRepository.findByCategoryId(categoryId);
        } else if (companyId != null) {
            products = productRepository.findByCompanyId(companyId);
        } else {
            products = productRepository.findAll();
        }

        return products.stream()
                .filter(product -> companyId == null || product.getCompany().getId().equals(companyId))
                .filter(product -> categoryId == null || product.getCategory().getId().equals(categoryId))
                .filter(product -> menuId == null || product.getMenus().stream().anyMatch(menu -> menu.getId().equals(menuId)))
                .peek(product -> product.getMenus().size())
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Product get(UUID id) {
        var product = productRepository.findById(id).orElseThrow();
        product.getMenus().size();
        return product;
    }

    @Override
    @Transactional
    public Product create(ProductRequest request) {
        var product = Product.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .imageUrl(request.imageUrl())
                .active(request.active())
                .category(findCategory(request.categoryId()))
                .company(findCompany(request.companyId()))
                .menus(resolveMenus(request.menuIds()))
                .build();

        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product update(UUID id, ProductRequest request) {
        var product = get(id);
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setImageUrl(request.imageUrl());
        product.setActive(request.active());
        product.setCategory(findCategory(request.categoryId()));
        product.setCompany(findCompany(request.companyId()));
        var menus = resolveMenus(request.menuIds());
        productRepository.deleteMenus(product.getId());
        productRepository.flush();
        product.setMenus(new HashSet<>(menus));
        return productRepository.save(product);
    }

    @Override
    public void delete(UUID id) {
        productRepository.deleteById(id);
    }

    private Category findCategory(UUID id) {
        return categoryRepository.findById(id).orElseThrow();
    }

    private Company findCompany(UUID id) {
        return companyRepository.findById(id).orElseThrow();
    }

    private Set<Menu> resolveMenus(List<UUID> menuIds) {
        if (menuIds == null || menuIds.isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar al menos un menú");
        }

        var distinctIds = new HashSet<>(menuIds);
        var resolved = new HashSet<Menu>();
        menuRepository.findAllById(distinctIds).forEach(resolved::add);

        if (resolved.size() != distinctIds.size()) {
            var foundIds = resolved.stream().map(Menu::getId).collect(Collectors.toSet());
            var missing = distinctIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .map(UUID::toString)
                    .toList();
            throw new NoSuchElementException("No se encontraron menús: " + String.join(", ", missing));
        }

        return resolved;
    }
}
