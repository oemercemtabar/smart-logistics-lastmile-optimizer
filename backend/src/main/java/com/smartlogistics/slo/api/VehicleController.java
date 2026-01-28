package com.smartlogistics.slo.api;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.smartlogistics.slo.api.dto.vehicle.CreateVehicleRequest;
import com.smartlogistics.slo.api.dto.vehicle.VehicleResponse;
import com.smartlogistics.slo.service.vehicle.VehicleService;

@RestController
@RequestMapping("/api/v1/vehicles")
public class VehicleController {

  private final VehicleService vehicleService;

  public VehicleController(VehicleService vehicleService) {
    this.vehicleService = vehicleService;
  }

  @PostMapping
  public ResponseEntity<VehicleResponse> create(@Valid @RequestBody CreateVehicleRequest req) {
    VehicleResponse created = vehicleService.create(req);
    return ResponseEntity.status(201).body(created);
  }

  @GetMapping
  public ResponseEntity<List<VehicleResponse>> list() {
    return ResponseEntity.ok(vehicleService.list());
  }

  @GetMapping("/{vehicleId}")
  public ResponseEntity<VehicleResponse> get(@PathVariable("vehicleId") UUID vehicleId) {
    return ResponseEntity.ok(vehicleService.get(vehicleId));
  }
}