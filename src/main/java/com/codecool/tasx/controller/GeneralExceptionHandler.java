package com.codecool.tasx.controller;

import com.codecool.tasx.exception.auth.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GeneralExceptionHandler {
  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<?> handleCustomUnauthorized() {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
      "error", "Unauthorized"
    ));
  }
}
