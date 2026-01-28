package com.smartlogistics.slo.api;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ProblemDetail handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
    String msg = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
    boolean duplicatePlate = msg != null && msg.contains("vehicles_plate_number_key");

    HttpStatus status = duplicatePlate ? HttpStatus.CONFLICT : HttpStatus.BAD_REQUEST;

    ProblemDetail pd = ProblemDetail.forStatusAndDetail(status,
        duplicatePlate ? "plateNumber already exists" : "data integrity violation");

    pd.setTitle(duplicatePlate ? "Duplicate resource" : "Invalid data");
    pd.setType(URI.create(duplicatePlate ? "https://errors.smartlogistics.dev/duplicate-vehicle-plate" :
        "https://errors.smartlogistics.dev/data-integrity"));
    pd.setProperty("timestamp", Instant.now().toString());
    pd.setProperty("path", req.getRequestURI());

    return pd;
  }
}