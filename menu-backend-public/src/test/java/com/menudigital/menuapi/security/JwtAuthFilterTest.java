package com.menudigital.menuapi.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

  @Mock private JwtService jwtService;
  @Mock private UserDetailsService userDetailsService;

  @InjectMocks private JwtAuthFilter jwtAuthFilter;

  @AfterEach
  void cleanContext() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void shouldReturnUnauthorizedWhenTokenExpired() throws ServletException, IOException {
    var request = new MockHttpServletRequest();
    request.setRequestURI("/api/secure-endpoint");
    request.addHeader("Authorization", "Bearer expired-token");

    var response = new MockHttpServletResponse();
    FilterChain chain = mock(FilterChain.class);

    doThrow(new ExpiredJwtException(null, null, "Token expired"))
        .when(jwtService)
        .extractUsername(anyString());

    jwtAuthFilter.doFilterInternal(request, response, chain);

    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
    assertThat(response.getContentAsString()).isEqualTo("{\"message\":\"Token expirado\"}");
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    verifyNoInteractions(chain);
  }
}
