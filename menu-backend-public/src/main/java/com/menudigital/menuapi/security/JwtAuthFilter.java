package com.menudigital.menuapi.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws ServletException, IOException {

    String header = req.getHeader(HttpHeaders.AUTHORIZATION);
    if (header == null || !header.startsWith("Bearer ")) {
      chain.doFilter(req, res);
      return;
    }

    String token = header.substring(7);

    try {
      String username = jwtService.extractUsername(token);
      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails ud = userDetailsService.loadUserByUsername(username);
        if (jwtService.isValid(token, ud)) {
          var auth =
              new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities());
          auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
          SecurityContextHolder.getContext().setAuthentication(auth);
        }
      }
    } catch (ExpiredJwtException e) {
      SecurityContextHolder.clearContext();
      log.warn("Expired JWT while accessing {}: {}", req.getRequestURI(), e.getMessage());
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      res.setContentType(MediaType.APPLICATION_JSON_VALUE);
      res.getWriter().write("{\"message\":\"Token expirado\"}");
      return;
    } catch (JwtException | IllegalArgumentException e) {
      SecurityContextHolder.clearContext();
      log.warn("Invalid JWT while accessing {}: {}", req.getRequestURI(), e.getMessage());
    }

    chain.doFilter(req, res);
  }
}
