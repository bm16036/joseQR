package com.menudigital.menuapi.menu.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.menudigital.menuapi.menu.domain.Company;
import com.menudigital.menuapi.menu.domain.Menu;
import com.menudigital.menuapi.menu.repo.CompanyRepository;
import com.menudigital.menuapi.menu.repo.MenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MenuServiceImplTest {

    @Autowired private MenuService menuService;
    @Autowired private CompanyRepository companyRepository;
    @Autowired private MenuRepository menuRepository;

    private Company company;

    @BeforeEach
    void setUp() {
        menuRepository.deleteAll();
        companyRepository.deleteAll();

        company =
                companyRepository.save(
                        Company.builder()
                                .businessName("Test Company")
                                .commercialName("Test Company")
                                .email("test@example.com")
                                .phone("123456789")
                                .build());

        menuRepository.save(Menu.builder().name("Inactive Menu").active(false).company(company).build());
        menuRepository.save(Menu.builder().name("Active Menu").active(true).company(company).build());
    }

    @Test
    void listShouldReturnAllMenusForCompany() {
        var menus = menuService.list(company.getId());

        assertThat(menus)
                .extracting(Menu::getName)
                .containsExactlyInAnyOrder("Inactive Menu", "Active Menu");
    }

    @Test
    void listActiveShouldReturnOnlyActiveMenusForCompany() {
        var menus = menuService.listActive(company.getId());

        assertThat(menus).hasSize(1);
        assertThat(menus.get(0).isActive()).isTrue();
        assertThat(menus.get(0).getName()).isEqualTo("Active Menu");
    }
}
