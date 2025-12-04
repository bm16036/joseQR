import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { CompanyService } from '../../core/services/company.service';
import { Company } from '../../core/models/company.model';
import { EMAIL_REGEX } from '../../core/constants/validation.constants';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-company-management',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './company-management.component.html',
  styleUrl: './company-management.component.scss'
})
export class CompanyManagementComponent {
  private readonly fb = inject(FormBuilder);
  private readonly companyService = inject(CompanyService);

  readonly companies = this.companyService.companies;
  readonly selectedCompanyId = signal<string | null>(null);
  readonly feedbackMessage = signal<string | null>(null);
  readonly isSaving = signal(false);
  readonly showQrModal = signal(false);
  readonly qrImageUrl = signal<string | null>(null);
  readonly menuLink = signal<string | null>(null);

  /** 🔥 BASE URL del backend para generar QR */
  readonly qrBaseUrl = `${environment.apiBaseUrl}/qrs/menu`;
  /** URL base del frontend para construir el enlace público del menú */
  readonly menuBaseUrl = `${environment.frontendBaseUrl}/menu`;

  readonly companyForm = this.fb.nonNullable.group({
    taxId: ['', [Validators.required, Validators.pattern(/^\d{11,13}$/)]],
    businessName: ['', [Validators.required, Validators.maxLength(120)]],
    commercialName: ['', [Validators.required, Validators.maxLength(80)]],
    email: ['', [Validators.required, Validators.pattern(EMAIL_REGEX)]],
    phone: [
      '',
      [Validators.required, Validators.pattern(/^[+()\d\s-]{7,20}$/)]
    ],
    logoUrl: [
      '',
      [Validators.required, Validators.pattern(/^(https?:\/\/).+/)]
    ]
  });

  constructor() {
    this.companyService.load().subscribe();
  }

  edit(company: Company) {
    this.selectedCompanyId.set(company.id);
    this.companyForm.patchValue(company);
  }

  cancelEdit() {
    this.selectedCompanyId.set(null);
    this.companyForm.reset({
      taxId: '',
      businessName: '',
      commercialName: '',
      email: '',
      phone: '',
      logoUrl: ''
    });
  }

  save() {
    if (this.companyForm.invalid) {
      this.companyForm.markAllAsTouched();
      return;
    }

    this.isSaving.set(true);
    const payload = this.companyForm.getRawValue();

    const request$ = this.selectedCompanyId()
      ? this.companyService.update(this.selectedCompanyId()!, payload)
      : this.companyService.create(payload);

    request$.subscribe({
      next: () => {
        this.feedbackMessage.set('Empresa guardada correctamente.');
        this.isSaving.set(false);
        this.cancelEdit();
      },
      error: () => {
        this.feedbackMessage.set('No se pudo guardar la empresa.');
        this.isSaving.set(false);
      }
    });
  }

  delete(companyId: string) {
    if (!confirm('¿Eliminar esta empresa? Los usuarios asociados perderán acceso.')) {
      return;
    }

    this.companyService.delete(companyId).subscribe();
  }

  /** 🔥 Método para abrir o descargar el QR */
  openQr(companyId: string) {
    const qrImageUrl = `${this.qrBaseUrl}/${companyId}`;
    const menuLink = `${this.menuBaseUrl}/${companyId}`;

    this.qrImageUrl.set(qrImageUrl);
    this.menuLink.set(menuLink);
    this.showQrModal.set(true);
  }

  closeQrModal() {
    this.showQrModal.set(false);
    this.qrImageUrl.set(null);
    this.menuLink.set(null);
  }

  async copyQrLink() {
    const url = this.menuLink();
    if (!url) {
      return;
    }

    try {
      await navigator.clipboard.writeText(url);
    } catch (error) {
      const textarea = document.createElement('textarea');
      textarea.value = url;
      textarea.style.position = 'fixed';
      textarea.style.opacity = '0';
      document.body.appendChild(textarea);
      textarea.select();
      document.execCommand('copy');
      document.body.removeChild(textarea);
    }
  }

  async downloadQrImage() {
    const url = this.qrImageUrl();
    if (!url) {
      return;
    }

    try {
      const response = await fetch(url);
      const blob = await response.blob();
      const objectUrl = URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = objectUrl;
      link.download = 'codigo-qr.jpg';
      link.click();
      URL.revokeObjectURL(objectUrl);
    } catch (error) {
      console.error('No se pudo descargar el QR', error);
    }
  }
}
