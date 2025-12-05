import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { forkJoin, map, Observable } from 'rxjs';

import { Category } from '../../../core/models/category.model';
import { Menu } from '../../../core/models/menu.model';
import { Product } from '../../../core/models/product.model';
import { environment } from '../../../../environments/environment';

export interface MenuDetails {
  menu: Menu | null;
  categories: (Category & { products: Product[] })[];
}

@Injectable({ providedIn: 'root' })
export class PublicMenuService {

  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiBaseUrl}/public`;

  getCategories(companyId: string): Observable<Category[]> {
    return this.http.get<Category[]>(`${this.baseUrl}/companies/${companyId}/categories`);
  }

  getMenus(companyId: string): Observable<Menu[]> {
    return this.http.get<Menu[]>(`${this.baseUrl}/companies/${companyId}/menu`);
  }

  getProductsByCategory(categoryId: string): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.baseUrl}/categories/${categoryId}/products`);
  }

  getProductsByCompany(companyId: string): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.baseUrl}/companies/${companyId}/products`);
  }

  getMenuDetails(menuId: string): Observable<MenuDetails> {
    return forkJoin({
      menus: this.getMenus(menuId),
      categories: this.getCategories(menuId),
      products: this.getProductsByCompany(menuId)
    }).pipe(
      map(({ menus, categories, products }) => {
        const selectedMenu = menus.find((menu) => menu.id === menuId) ?? null;
        const filteredProducts = products.filter((product) => product.menuIds?.includes(menuId));

        const categoriesWithProducts = categories
          .map((category) => ({
            ...category,
            products: filteredProducts
              .filter((product) => product.categoryId === category.id)
              .sort((a, b) => a.name.localeCompare(b.name))
          }))
          .filter((category) => category.products.length > 0)
          .sort((a, b) => a.name.localeCompare(b.name));

        return { menu: selectedMenu, categories: categoriesWithProducts } satisfies MenuDetails;
      })
    );
  }
}
