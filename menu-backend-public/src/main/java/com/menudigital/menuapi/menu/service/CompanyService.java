package com.menudigital.menuapi.menu.service; 
import com.menudigital.menuapi.menu.domain.Company; 
import java.util.*; public interface CompanyService{ List<Company> list(); Company get(UUID id); Company create(Company c); Company update(UUID id, Company c); void delete(UUID id);}