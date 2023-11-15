package com.codecool.tasx.service.security;

import com.codecool.tasx.exception.auth.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;

public class MockAuthProvider implements AuthProvider {
  @Override
  public Long getUserId(HttpServletRequest request) {
    //TODO: get user from actual auth context
    try {
      return Long.valueOf(request.getHeader("Authorization").split(" ")[1]);
    } catch (Exception e) {
      throw new UnauthorizedException();
    }
  }
}
