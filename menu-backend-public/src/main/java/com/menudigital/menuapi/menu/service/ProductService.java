package com.menudigital.menuapi.menu.service;

import com.menudigital.menuapi.menu.domain.Product;
import com.menudigital.menuapi.menu.dto.ProductRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ProductService {
    List<Product> list(UUID companyId, UUID categoryId, UUID menuId);
    Map<UUID, Map<UUID, List<Product>>> listGroupedByMenuAndCategory(UUID companyId);
    Product get(UUID id);
    Product create(ProductRequest request);
    Product update(UUID id, ProductRequest request);
    void delete(UUID id);
}
