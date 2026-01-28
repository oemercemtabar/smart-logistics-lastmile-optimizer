package com.smartlogistics.slo.service.driver;

public class DriverAlreadyExistsException extends RuntimeException {
  public DriverAlreadyExistsException(String email, Throwable cause) {
    super("Driver already exists with email: " + email, cause);
  }
}