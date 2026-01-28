package com.smartlogistics.slo.service.driver;

import java.util.UUID;

public class DriverNotFoundException extends RuntimeException {
  public DriverNotFoundException(UUID id) {
    super("Driver not found: " + id);
  }
}