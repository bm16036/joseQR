package com.menudigital.menuapi.menu.service.impl;
import com.menudigital.menuapi.menu.domain.Company; import com.menudigital.menuapi.menu.repo.CompanyRepository; import com.menudigital.menuapi.menu.service.CompanyService; import lombok.RequiredArgsConstructor; import org.springframework.stereotype.Service; import java.util.*;
@Service @RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
  private final CompanyRepository repo;
  public java.util.List<Company> list(){ return repo.findAll(); }
  public Company get(UUID id){ return repo.findById(id).orElseThrow(); }
  
  public Company create(Company c){ return repo.save(c); }
  public Company update(UUID id, Company c){ var db=get(id); db.setTaxId(c.getTaxId()); db.setBusinessName(c.getBusinessName()); db.setCommercialName(c.getCommercialName()); db.setEmail(c.getEmail()); db.setPhone(c.getPhone()); db.setLogoUrl(c.getLogoUrl()); return repo.save(db); }
  public void delete(UUID id){ repo.deleteById(id); }
  
  
}
