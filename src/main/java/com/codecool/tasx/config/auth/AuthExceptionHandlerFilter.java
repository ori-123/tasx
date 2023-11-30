package com.codecool.tasx.config.auth;

import com.codecool.tasx.exception.auth.UnauthorizedException;
import com.codecool.tasx.service.auth.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class AuthExceptionHandlerFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
    @NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
    @NonNull FilterChain filterChain)
    throws ServletException, IOException {
    try {
      filterChain.doFilter(request, response);
    } catch (Exception e) {
      throw new UnauthorizedException();
    }
  }
}
