package com.menudigital.menuapi.config;

import com.menudigital.menuapi.menu.domain.Company;
import com.menudigital.menuapi.menu.repo.CompanyRepository;
import com.menudigital.menuapi.security.Role;
import com.menudigital.menuapi.security.RoleRepository;
import com.menudigital.menuapi.security.UserEntity;
import com.menudigital.menuapi.security.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;

    @Bean
    CommandLineRunner seed(CompanyRepository companyRepo, UserRepository userRepo) {
        return args -> {

            // 🏢 Si no existen empresas, se crea una de prueba
            if (companyRepo.count() == 0) {
                var company = Company.builder()
                        .taxId("0614-123456-001-9")
                        .businessName("Mi Restaurante, S.A. de C.V.")
                        .commercialName("Mi Restaurante")
                        .email("info@resto.test")
                        .phone("+503 2222-2222")
                        .logoUrl(null)
                        .build();

                company = companyRepo.save(company);
            }

            // 🔐 Crear roles si no existen
            var rolAdmin = roleRepository.findByNombre("ADMIN")
                    .orElseGet(() -> roleRepository.save(
                            Role.builder()
                                    .nombre("ADMIN")
                                    .descripcion("Administrador del sistema")
                                    .build()
                    ));

            var rolUser = roleRepository.findByNombre("USER")
                    .orElseGet(() -> roleRepository.save(
                            Role.builder()
                                    .nombre("USER")
                                    .descripcion("Usuario estándar")
                                    .build()
                    ));

            // 👤 Crear usuario admin si no existe
            if (userRepo.findByEmail("admin@resto.test").isEmpty()) {
                var company = companyRepo.findAll().stream().findFirst().orElse(null);

                var admin = UserEntity.builder()
                        .username("admin")
                        .passwordHash(encoder.encode("admin123"))
                        .email("admin@resto.test")
                        .role(rolAdmin) // ✅ ahora usa la entidad Role
                        .company(company)
                        .active(true)
                        .build();

                userRepo.save(admin);
                System.out.println("✅ Usuario administrador creado: admin@resto.test / admin123");
            }
        };
    }
}
