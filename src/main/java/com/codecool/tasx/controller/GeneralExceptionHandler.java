package com.codecool.tasx.controller;

import com.codecool.tasx.exception.auth.UnauthorizedException;
import com.codecool.tasx.exception.user.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GeneralExceptionHandler {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<?> handleCustomUnauthorized(UnauthorizedException e) {
    logger.error(e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
      "error", "Unauthorized"
    ));
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<?> handleUserNotFound(UserNotFoundException e) {
    logger.error(e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
      "error", "Unauthorized"
    ));
  }
}
