package com.menudigital.menuapi.menu.dto;

import com.menudigital.menuapi.menu.domain.Company;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Payload utilizado para crear o actualizar compañías desde el API.
 */
public record CompanyRequest(
        @NotBlank @Size(max = 30) String taxId,
        @NotBlank @Size(max = 120) String businessName,
        @NotBlank @Size(max = 120) String commercialName,
        @Email @Size(max = 120) String email,
        @Size(max = 30) String phone,
        @Size(max = 255) String logoUrl
) {
    public Company toEntity() {
        return Company.builder()
                .taxId(taxId)
                .businessName(businessName)
                .commercialName(commercialName)
                .email(email)
                .phone(phone)
                .logoUrl(logoUrl)
                .build();
    }
}
