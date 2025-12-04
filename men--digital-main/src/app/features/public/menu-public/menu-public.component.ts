import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { PublicMenuService } from './public-menu.service';

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
  categories = signal<any[]>([]);
  menu = signal<any[]>([]);
  products = signal<any[]>([]);
  selectedCategoryId = signal<string | null>(null);

  constructor() {
    this.companyId = this.route.snapshot.paramMap.get('companyId') ?? '';

    this.loadCategories();
    this.loadMenu();
  }

  loadCategories() {
    this.publicMenuService.getCategories(this.companyId).subscribe((data: any[]) => {
      this.categories.set(data);
    });
  }

  loadMenu() {
    this.publicMenuService.getMenu(this.companyId).subscribe((data: any[]) => {
      this.menu.set(data);
    });
  }

  loadProductsByCategory(categoryId: string) {
    this.selectedCategoryId.set(categoryId);
    this.publicMenuService.getProductsByCategory(categoryId).subscribe((data: any[]) => {
      this.products.set(data);
    });
  }
}
