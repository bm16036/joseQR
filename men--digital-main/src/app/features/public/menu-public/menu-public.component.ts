import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { PublicMenu, PublicMenuService } from './public-menu.service';

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
  menu = signal<PublicMenu[]>([]);
  isLoading = signal<boolean>(true);

  constructor() {
    this.companyId = this.route.snapshot.paramMap.get('companyId') ?? '';

    this.loadMenuHierarchy();
  }

  loadMenuHierarchy() {
    this.isLoading.set(true);

    this.publicMenuService.getMenuHierarchy(this.companyId).subscribe({
      next: (hierarchy) => {
        this.menu.set(hierarchy);
        this.isLoading.set(false);
      },
      error: () => this.isLoading.set(false)
    });
  }
}
