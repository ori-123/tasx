package com.codecool.tasx.service.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface AuthProvider {
  Long getUserId(HttpServletRequest request);
}
