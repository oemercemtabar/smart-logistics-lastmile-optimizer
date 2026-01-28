package com.smartlogistics.slo.service.vehicle;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartlogistics.slo.api.dto.vehicle.CreateVehicleRequest;
import com.smartlogistics.slo.api.dto.vehicle.VehicleResponse;
import com.smartlogistics.slo.domain.vehicle.VehicleEntity;
import com.smartlogistics.slo.domain.vehicle.VehicleRepository;
import com.smartlogistics.slo.service.exception.ConflictException;
import com.smartlogistics.slo.service.exception.NotFoundException;

@Service
public class VehicleService {

  private final VehicleRepository vehicleRepository;

  public VehicleService(VehicleRepository vehicleRepository) {
    this.vehicleRepository = vehicleRepository;
  }

  @Transactional
  public VehicleResponse create(CreateVehicleRequest req) {
    String plate = req.plateNumber().trim().toUpperCase();

    if (vehicleRepository.existsByPlateNumber(plate)) {
      throw new ConflictException(
          "VEHICLE_PLATE_EXISTS",
          "Vehicle with plateNumber '" + plate + "' already exists"
      );
    }

    Instant now = Instant.now();

    VehicleEntity entity = VehicleEntity.builder()
        .plateNumber(plate)
        .capacityParcels(req.capacityParcels())
        .capacityKg(req.capacityKg())
        .active(Boolean.TRUE.equals(req.active()))
        .createdAt(now)
        .updatedAt(now)
        .build();

    VehicleEntity saved = vehicleRepository.save(entity);
    return toResponse(saved);
  }

  @Transactional(readOnly = true)
  public List<VehicleResponse> list() {
    return vehicleRepository.findAll().stream()
        .map(this::toResponse)
        .toList();
  }

  @Transactional(readOnly = true)
  public VehicleResponse get(UUID id) {
    VehicleEntity entity = vehicleRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(
            "VEHICLE_NOT_FOUND",
            "Vehicle '" + id + "' not found"
        ));
    return toResponse(entity);
  }

  private VehicleResponse toResponse(VehicleEntity v) {
    return new VehicleResponse(
        v.getId(),
        v.getPlateNumber(),
        v.getCapacityParcels(),
        v.getCapacityKg(),
        v.isActive(),
        v.getCreatedAt(),
        v.getUpdatedAt()
    );
  }
}