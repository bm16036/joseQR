import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { forkJoin } from 'rxjs';

import { PublicMenuService } from './public-menu.service';

type ProductView = {
  id: string;
  nombre: string;
  descripcion?: string;
  precio?: number;
};

type CategoryView = {
  id: string;
  nombre: string;
  descripcion?: string;
  productos: ProductView[];
};

type MenuView = {
  id: string;
  nombre: string;
  descripcion?: string;
  categorias: CategoryView[];
};

@Component({
  selector: 'app-menu-public',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './menu-public.component.html',
  styleUrls: ['./menu-public.component.scss']
})
export class MenuPublicComponent {

  private readonly route = inject(ActivatedRoute);
  private readonly publicMenuService = inject(PublicMenuService);

  companyId = '';
  menu = signal<MenuView[]>([]);
  isLoading = signal<boolean>(true);

  constructor() {
    this.companyId = this.route.snapshot.paramMap.get('companyId') ?? '';

    this.loadMenuHierarchy();
  }

  loadMenuHierarchy() {
    this.isLoading.set(true);

    forkJoin({
      menus: this.publicMenuService.getMenu(this.companyId),
      categories: this.publicMenuService.getCategories(this.companyId),
      products: this.publicMenuService.getProductsByCompany(this.companyId)
    }).subscribe({
      next: ({ menus, categories, products }) => {
        const hierarchy: MenuView[] = menus
          .map((menu) => {
            const normalizedMenuName = (menu as any).nombre ?? (menu as any).name ?? '';
            const normalizedMenuDescription = (menu as any).descripcion ?? (menu as any).description;

            const categoriesWithProducts: CategoryView[] = categories
              .map((category) => {
                const relatedProducts: ProductView[] = products
                  .filter((product) => (product.menuIds ?? []).includes(menu.id) && product.categoryId === category.id)
                  .map((product) => ({
                    ...product,
                    nombre: (product as any).nombre ?? (product as any).name ?? '',
                    descripcion: (product as any).descripcion ?? (product as any).description,
                    precio: (product as any).precio ?? (product as any).price
                  }));

                if (relatedProducts.length === 0) {
                  return null;
                }

                return {
                  ...category,
                  nombre: (category as any).nombre ?? (category as any).name ?? '',
                  descripcion: (category as any).descripcion ?? (category as any).description,
                  productos: relatedProducts
                } satisfies CategoryView;
              })
              .filter((category): category is CategoryView => Boolean(category));

            return {
              ...menu,
              nombre: normalizedMenuName,
              descripcion: normalizedMenuDescription,
              categorias: categoriesWithProducts
            } satisfies MenuView;
          })
          .filter((menu) => menu.categorias.length > 0);

        this.menu.set(hierarchy);
        this.isLoading.set(false);
      },
      error: () => this.isLoading.set(false)
    });
  }
}
