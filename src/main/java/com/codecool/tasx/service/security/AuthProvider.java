package com.codecool.tasx.service.security;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthProvider {
  Long getUserId(HttpServletRequest request);
}
