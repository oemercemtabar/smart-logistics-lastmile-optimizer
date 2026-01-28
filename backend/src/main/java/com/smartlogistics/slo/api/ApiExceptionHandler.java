package com.smartlogistics.slo.api;

import java.time.Instant;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.smartlogistics.slo.service.exception.ConflictException;
import com.smartlogistics.slo.service.exception.NotFoundException;

@RestControllerAdvice
public class ApiExceptionHandler {

  record ApiError(
      Instant timestamp,
      int status,
      String error,
      String code,
      String message,
      String path
  ) {}

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ApiError> handleConflict(ConflictException ex, HttpServletRequest req) {
    return build(HttpStatus.CONFLICT, ex.getCode(), ex.getMessage(), req.getRequestURI());
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ApiError> handleNotFound(NotFoundException ex, HttpServletRequest req) {
    return build(HttpStatus.NOT_FOUND, ex.getCode(), ex.getMessage(), req.getRequestURI());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
    String msg = ex.getBindingResult().getFieldErrors().stream()
        .map(this::formatFieldError)
        .collect(Collectors.joining("; "));
    return build(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", msg, req.getRequestURI());
  }

  private String formatFieldError(FieldError fe) {
    return fe.getField() + ": " + (fe.getDefaultMessage() == null ? "invalid" : fe.getDefaultMessage());
  }

  private ResponseEntity<ApiError> build(HttpStatus status, String code, String message, String path) {
    ApiError body = new ApiError(
        Instant.now(),
        status.value(),
        status.getReasonPhrase(),
        code,
        message,
        path
    );
    return ResponseEntity.status(status).body(body);
  }
}