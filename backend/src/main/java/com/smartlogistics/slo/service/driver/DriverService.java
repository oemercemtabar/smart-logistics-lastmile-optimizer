package com.smartlogistics.slo.service.driver;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartlogistics.slo.api.dto.driver.CreateDriverRequest;
import com.smartlogistics.slo.api.dto.driver.DriverResponse;
import com.smartlogistics.slo.domain.driver.DriverEntity;
import com.smartlogistics.slo.domain.driver.DriverRepository;
import com.smartlogistics.slo.domain.vehicle.VehicleEntity;
import com.smartlogistics.slo.domain.vehicle.VehicleRepository;
import com.smartlogistics.slo.service.exception.ConflictException;
import com.smartlogistics.slo.service.exception.NotFoundException;

@Service
public class DriverService {

    private final VehicleRepository vehicleRepository;

  private final DriverRepository driverRepository;

  public DriverService(DriverRepository driverRepository, VehicleRepository vehicleRepository) {
    this.driverRepository = driverRepository;
    this.vehicleRepository = vehicleRepository;
  }

  @Transactional
  public DriverResponse create(CreateDriverRequest req) {
    String email = req.email().trim().toLowerCase();
    String fullName = req.fullName().trim();

    if (driverRepository.existsByEmail(email)) {
      throw new ConflictException(
          "DRIVER_EMAIL_EXISTS",
          "Driver with email '" + email + "' already exists"
      );
    }

    DriverEntity entity = DriverEntity.builder()
        .fullName(fullName)
        .email(email)
        .shiftStart(req.shiftStart())
        .shiftEnd(req.shiftEnd())
        .active(Boolean.TRUE.equals(req.active()))
        .build();

    DriverEntity saved = driverRepository.save(entity);
    return toResponse(saved);
  }

  @Transactional(readOnly = true)
  public List<DriverResponse> list() {
    return driverRepository.findAll().stream().map(this::toResponse).toList();
  }

  @Transactional(readOnly = true)
  public DriverResponse get(UUID driverId) {
    DriverEntity entity = driverRepository.findById(driverId)
        .orElseThrow(() -> new NotFoundException(
            "DRIVER_NOT_FOUND",
            "Driver '" + driverId + "' not found"
        ));
    return toResponse(entity);
  }

  @Transactional
  public DriverResponse assignVehicle(UUID driverId, UUID vehicleId) {
    DriverEntity driver = driverRepository.findById(driverId)
        .orElseThrow(() -> new NotFoundException("DRIVER_NOT_FOUND", "Driver '" + driverId + "' not found"));

    VehicleEntity vehicle = vehicleRepository.findById(vehicleId)
        .orElseThrow(() -> new NotFoundException("VEHICLE_NOT_FOUND", "Vehicle '" + vehicleId + "' not found"));

    // if you want 1 driver per vehicle:
    if (driverRepository.existsByVehicle_Id(vehicleId) &&
        (driver.getVehicle() == null || !vehicleId.equals(driver.getVehicle().getId()))) {
      throw new ConflictException("VEHICLE_ALREADY_ASSIGNED", "Vehicle '" + vehicleId + "' is already assigned");
    }

    driver.setVehicle(vehicle);
    DriverEntity saved = driverRepository.save(driver);
    return toResponse(saved);
  }

  @Transactional
  public DriverResponse unassignVehicle(UUID driverId) {
    DriverEntity driver = driverRepository.findById(driverId)
        .orElseThrow(() -> new NotFoundException("DRIVER_NOT_FOUND", "Driver '" + driverId + "' not found"));

    driver.setVehicle(null);
    DriverEntity saved = driverRepository.save(driver);
    return toResponse(saved);
  }

  private DriverResponse toResponse(DriverEntity e) {
    UUID vehicleId = (e.getVehicle() == null) ? null : e.getVehicle().getId();
    return new DriverResponse(
        e.getId(),
        e.getFullName(),
        e.getEmail(),
        e.getShiftStart(),
        e.getShiftEnd(),
        vehicleId,
        e.isActive(),
        e.getCreatedAt(),
        e.getUpdatedAt()
    );
  }
}