package com.menudigital.menuapi.config;
import com.menudigital.menuapi.security.JwtAuthFilter; import lombok.RequiredArgsConstructor; import org.springframework.context.annotation.*; import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*; import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity; import org.springframework.security.config.http.SessionCreationPolicy; import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain; import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;
import java.util.List;
@Configuration @EnableMethodSecurity @RequiredArgsConstructor
public class SecurityConfig {
  private final JwtAuthFilter jwtAuthFilter; private final UserDetailsService userDetailsService;
  @Bean public SecurityFilterChain chain(HttpSecurity http) throws Exception {
    http.csrf(csrf->csrf.disable()).cors(c->c.configurationSource(corsConfigurationSource())).sessionManagement(sm->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(a->a.requestMatchers("/api/auth/**","/v3/api-docs/**","/swagger-ui.html","/swagger-ui/**","/api/companies/**", "/api/qrs/**").permitAll()
      .requestMatchers(HttpMethod.GET,"/api/public/**").permitAll().anyRequest().authenticated())
      .authenticationProvider(provider()).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); return http.build();
  }
  @Bean public AuthenticationProvider provider(){ var p=new DaoAuthenticationProvider(); p.setUserDetailsService(userDetailsService); p.setPasswordEncoder(encoder()); return p; }
  @Bean public PasswordEncoder encoder(){ return new BCryptPasswordEncoder(); }
  @Bean public AuthenticationManager am(AuthenticationConfiguration c) throws Exception { return c.getAuthenticationManager(); }
  @Bean public CorsConfigurationSource corsConfigurationSource(){ var cfg=new CorsConfiguration(); cfg.setAllowedOrigins(List.of(System.getenv().getOrDefault("CORS_ORIGIN","http://localhost:4200"))); cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS")); cfg.setAllowedHeaders(List.of("*")); cfg.setAllowCredentials(true); var src=new UrlBasedCorsConfigurationSource(); src.registerCorsConfiguration("/**",cfg); return src; }
}
