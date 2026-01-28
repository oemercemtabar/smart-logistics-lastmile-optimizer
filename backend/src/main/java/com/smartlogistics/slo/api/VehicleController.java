package com.smartlogistics.slo.api;

import com.smartlogistics.slo.api.dto.vehicle.CreateVehicleRequest;
import com.smartlogistics.slo.api.dto.vehicle.VehicleResponse;
import com.smartlogistics.slo.service.vehicle.VehicleService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vehicles")
public class VehicleController {
  private final VehicleService service;

  public VehicleController(VehicleService service) {
    this.service = service;
  }

  @GetMapping
  public List<VehicleResponse> list() {
    return service.list();
  }

  @PostMapping
  public VehicleResponse create(@Valid @RequestBody CreateVehicleRequest body) {
    return service.create(body);
  }

  @GetMapping("/{id}")
  public VehicleResponse get(@PathVariable UUID id) {
    return service.get(id);
  }
}