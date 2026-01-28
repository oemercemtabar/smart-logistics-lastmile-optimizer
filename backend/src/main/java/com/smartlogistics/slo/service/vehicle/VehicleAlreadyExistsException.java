package com.smartlogistics.slo.service.vehicle;

public class VehicleAlreadyExistsException extends RuntimeException {
  public VehicleAlreadyExistsException(String plateNumber, Throwable cause) {
    super("Vehicle already exists with plateNumber: " + plateNumber, cause);
  }
}