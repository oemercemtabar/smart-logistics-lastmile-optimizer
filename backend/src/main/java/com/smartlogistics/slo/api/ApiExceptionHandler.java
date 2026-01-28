package com.smartlogistics.slo.api;

import com.smartlogistics.slo.service.driver.DriverAlreadyExistsException;
import com.smartlogistics.slo.service.driver.DriverNotFoundException;
import com.smartlogistics.slo.service.vehicle.VehicleAlreadyExistsException;
import com.smartlogistics.slo.service.vehicle.VehicleNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler({VehicleNotFoundException.class, DriverNotFoundException.class})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ProblemDetail notFound(RuntimeException ex) {
    var pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    pd.setProperty("timestamp", Instant.now());
    return pd;
  }

  @ExceptionHandler({VehicleAlreadyExistsException.class, DriverAlreadyExistsException.class})
  @ResponseStatus(HttpStatus.CONFLICT)
  public ProblemDetail conflict(RuntimeException ex) {
    var pd = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    pd.setProperty("timestamp", Instant.now());
    return pd;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ProblemDetail validation(MethodArgumentNotValidException ex) {
    var pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
    pd.setProperty("timestamp", Instant.now());
    pd.setProperty("errors",
        ex.getBindingResult().getFieldErrors().stream()
            .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
            .toList()
    );
    return pd;
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ProblemDetail constraintViolation(ConstraintViolationException ex) {
    var pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    pd.setProperty("timestamp", Instant.now());
    return pd;
  }
}