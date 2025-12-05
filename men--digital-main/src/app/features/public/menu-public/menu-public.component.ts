import { CommonModule } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { MenuDetails, PublicMenuService } from './public-menu.service';

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

  readonly menuId = this.route.snapshot.paramMap.get('menuId') ?? '';
  readonly isLoading = signal(false);
  readonly error = signal<string | null>(null);
  readonly details = signal<MenuDetails | null>(null);

  readonly hasProducts = computed(() => {
    const info = this.details();
    return !!info && info.categories.some((category) => category.products.length > 0);
  });

  constructor() {
    this.loadMenu();
  }

  private loadMenu() {
    if (!this.menuId) {
      this.error.set('No se encontró el menú solicitado.');
      return;
    }

    this.isLoading.set(true);
    this.error.set(null);

    this.publicMenuService.getMenuDetails(this.menuId).subscribe({
      next: (data) => {
        if (!data.menu) {
          this.error.set('No se encontró el menú solicitado.');
        }

        this.details.set(data);
        this.isLoading.set(false);
      },
      error: () => {
        this.error.set('No se pudo cargar el menú. Inténtalo de nuevo más tarde.');
        this.isLoading.set(false);
      }
    });
  }
}
