package com.menudigital.menuapi.menu.controller;

import com.menudigital.menuapi.menu.dto.CompanyRequest;
import com.menudigital.menuapi.menu.dto.CompanyResponse;
import com.menudigital.menuapi.menu.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService service;

    @GetMapping
    public List<CompanyResponse> list() {
        return service.list().stream().map(CompanyResponse::from).toList();
    }

    @GetMapping("/{id}")
    public CompanyResponse get(@PathVariable UUID id) {
        return CompanyResponse.from(service.get(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyResponse create(@Valid @RequestBody CompanyRequest request) {
        var company = request.toEntity();
        return CompanyResponse.from(service.create(company));
    }

    @PutMapping("/{id}")
    public CompanyResponse update(@PathVariable UUID id, @Valid @RequestBody CompanyRequest request) {
        var company = request.toEntity();
        return CompanyResponse.from(service.update(id, company));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
