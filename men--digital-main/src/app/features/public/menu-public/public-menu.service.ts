import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class PublicMenuService {

  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiBaseUrl}/public`;

  getCategories(companyId: string) {
    return this.http.get<any[]>(`${this.baseUrl}/companies/${companyId}/categories`);
  }

  getMenu(companyId: string) {
    return this.http.get<any[]>(`${this.baseUrl}/companies/${companyId}/menu`);
  }

  getProductsByCategory(categoryId: string) {
    return this.http.get<any[]>(`${this.baseUrl}/categories/${categoryId}/products`);
  }
}
