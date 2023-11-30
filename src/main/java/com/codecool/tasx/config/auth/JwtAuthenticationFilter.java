package com.codecool.tasx.config.auth;

import com.codecool.tasx.exception.auth.UnauthorizedException;
import com.codecool.tasx.service.auth.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * {@inheritDoc}
 * - Parses JWT access token from Authorization header<br>
 * - Extracts subject (email) from token<br>
 * - Reads {@link UserDetails} from database<br>
 * - Verifies JWT (expiration, signature), cross-references {@link UserDetails}<br>
 * - Creates internal {@link UsernamePasswordAuthenticationToken} from {@link UserDetails}
 * and adds it to {@link SecurityContextHolder}
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  @Autowired
  public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
    this.jwtService = jwtService;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(
    @NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
    @NonNull FilterChain filterChain)
    throws ServletException, IOException {
    try {
      final String authHeader = request.getHeader("Authorization");
      final String accessTokenString;
      final String userEmail;

      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        filterChain.doFilter(request, response);
        return;
      }

      accessTokenString = authHeader.split(" ")[1];

      userEmail = jwtService.extractSubjectFromAccessToken(accessTokenString);
      if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

        if (jwtService.isAccessTokenValid(accessTokenString, userDetails)) {
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

          SecurityContextHolder.getContext().setAuthentication(authToken);
        } else {
          throw new UnauthorizedException();
        }
      }
      filterChain.doFilter(request, response);
    } catch (Exception e) {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.getWriter().write("{\"error\":\"Unauthorized\"}");
    }
  }
}
