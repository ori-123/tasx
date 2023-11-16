package com.codecool.tasx.controller;

import com.codecool.tasx.exception.auth.UnauthorizedException;
import com.codecool.tasx.exception.company.CompanyNotFoundException;
import com.codecool.tasx.exception.company.DuplicateCompanyJoinRequestException;
import com.codecool.tasx.exception.company.UserAlreadyInCompanyException;
import com.codecool.tasx.exception.user.UserNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
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

  @ExceptionHandler(CompanyNotFoundException.class)
  public ResponseEntity<?> handleCompanyNotFound(CompanyNotFoundException e) {
    logger.error(e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
      "error", "The requested company was not found"));
  }

  @ExceptionHandler(UserAlreadyInCompanyException.class)
  public ResponseEntity<?> handleUserAlreadyInCompany(UserAlreadyInCompanyException e) {
    logger.error(e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
      "error", "User is already in the requested company"));
  }

  @ExceptionHandler(DuplicateCompanyJoinRequestException.class)
  public ResponseEntity<?> handleDuplicateCompanyJoinRequest(
    DuplicateCompanyJoinRequestException e) {
    logger.error(e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
      "error", "Join request already exists with the provided details"));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<?> handleDuplicateFields(ConstraintViolationException e) {
    logger.error(e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
      "error", "Company with requested " + e.getConstraintName() + " already exists"));
  }
}
