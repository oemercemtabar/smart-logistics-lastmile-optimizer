package com.smartlogistics.slo.api;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.smartlogistics.slo.api.dto.driver.CreateDriverRequest;
import com.smartlogistics.slo.api.dto.driver.DriverResponse;
import com.smartlogistics.slo.service.driver.DriverService;

@RestController
@RequestMapping("/api/v1/drivers")
public class DriverController {

  private final DriverService driverService;

  public DriverController(DriverService driverService) {
    this.driverService = driverService;
  }

  @PostMapping
  public ResponseEntity<DriverResponse> create(@Valid @RequestBody CreateDriverRequest req) {
    DriverResponse created = driverService.create(req);
    return ResponseEntity.status(201).body(created);
  }

  @GetMapping
  public ResponseEntity<List<DriverResponse>> list() {
    return ResponseEntity.ok(driverService.list());
  }

  @GetMapping("/{driverId}")
  public ResponseEntity<DriverResponse> get(@PathVariable UUID driverId) {
    return ResponseEntity.ok(driverService.get(driverId));
  }

  @PutMapping("/{driverId}/vehicle/{vehicleId}")
  public ResponseEntity<DriverResponse> assignVehicle(@PathVariable UUID driverId, @PathVariable UUID vehicleId) {
    return ResponseEntity.ok(driverService.assignVehicle(driverId, vehicleId));
  }

  @DeleteMapping("/{driverId}/vehicle")
  public ResponseEntity<DriverResponse> unassignVehicle(@PathVariable UUID driverId) {
    return ResponseEntity.ok(driverService.unassignVehicle(driverId));
  }
  
}