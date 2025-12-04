package com.menudigital.menuapi.menu.dto;

import com.menudigital.menuapi.menu.domain.Company;

import java.time.Instant;
import java.util.UUID;

/**
 * Respuesta serializada para exponer compañías hacia el frontend.
 */
public record CompanyResponse(
        UUID id,
        String taxId,
        String businessName,
        String commercialName,
        String email,
        String phone,
        String logoUrl,
        Instant createdAt,
        Instant updatedAt
) {
    public static CompanyResponse from(Company company) {
        return new CompanyResponse(
                company.getId(),
                company.getTaxId(),
                company.getBusinessName(),
                company.getCommercialName(),
                company.getEmail(),
                company.getPhone(),
                company.getLogoUrl(),
                company.getCreatedAt(),
                company.getUpdatedAt()
        );
    }
}
