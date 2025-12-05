import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { map, Observable } from 'rxjs';

export type PublicMenuProduct = {
  id: string;
  nombre: string;
  descripcion?: string;
  precio?: number;
};

export type PublicMenuCategory = {
  id: string;
  nombre: string;
  descripcion?: string;
  productos: PublicMenuProduct[];
};

export type PublicMenu = {
  id: string;
  nombre: string;
  descripcion?: string;
  categorias: PublicMenuCategory[];
};

@Injectable({ providedIn: 'root' })
export class PublicMenuService {

  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiBaseUrl}/public`;

  getMenuHierarchy(companyId: string): Observable<PublicMenu[]> {
    return this.http
      .get<any[]>(`${this.baseUrl}/companies/${companyId}/menu/hierarchy`)
      .pipe(
        map((menus) =>
          menus
            .map((menu) => ({
              id: menu.id,
              nombre: (menu as any).nombre ?? (menu as any).name ?? '',
              descripcion: (menu as any).descripcion ?? (menu as any).description,
              categorias: ((menu as any).categorias ?? menu.categories ?? [])
                .map((category: any) => ({
                  id: category.id,
                  nombre: (category as any).nombre ?? (category as any).name ?? '',
                  descripcion: (category as any).descripcion ?? (category as any).description,
                  productos: ((category as any).productos ?? category.products ?? []).map((product: any) => ({
                    id: product.id,
                    nombre: (product as any).nombre ?? (product as any).name ?? '',
                    descripcion: (product as any).descripcion ?? (product as any).description,
                    precio: (product as any).precio ?? (product as any).price
                  }))
                }))
                .filter((category: PublicMenuCategory) => category.productos.length > 0)
            }))
            .filter((menu) => menu.categorias.some((category: PublicMenuCategory) => category.productos.length > 0))
        )
      );
  }
}
