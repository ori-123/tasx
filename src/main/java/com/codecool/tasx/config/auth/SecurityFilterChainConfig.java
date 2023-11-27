package com.codecool.tasx.config.auth;

import com.codecool.tasx.filter.auth.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityFilterChainConfig {
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final AuthenticationProvider authenticationProvider;

  @Autowired
  public SecurityFilterChainConfig(
    JwtAuthenticationFilter jwtAuthenticationFilter,
    AuthenticationProvider authenticationProvider) {
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    this.authenticationProvider = authenticationProvider;
  }

  /**
   * {@inheritDoc}
   * @CSRF: disabled
   * @White-list: "/api/v1/auth/**", "/error" (access allowed without authentication)
   * @Session: Stateless (no {@link jakarta.servlet.http.HttpSession})
   * @Provider: Use the {@link AuthenticationProvider} defined in {@link SecurityConfig}
   * @Authentication: Use {@link JwtAuthenticationFilter} for all non-whitelisted endpoints
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
      .csrf(csrfConfigurer -> csrfConfigurer.disable())
      .authorizeHttpRequests(authorizeRequestsConfigurer -> authorizeRequestsConfigurer
        .requestMatchers("/api/v1/auth/**", "/error").permitAll()
        .anyRequest().authenticated())
      .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authenticationProvider(authenticationProvider)
      .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return httpSecurity.build();
  }
}
