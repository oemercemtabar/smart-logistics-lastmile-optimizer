package com.smartlogistics.slo.api;

import com.smartlogistics.slo.api.dto.driver.CreateDriverRequest;
import com.smartlogistics.slo.api.dto.driver.DriverResponse;
import com.smartlogistics.slo.service.driver.DriverService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/drivers")
public class DriverController {
  private final DriverService service;

  public DriverController(DriverService service) {
    this.service = service;
  }

  @GetMapping
  public List<DriverResponse> list() {
    return service.list();
  }

  @PostMapping
  public DriverResponse create(@Valid @RequestBody CreateDriverRequest body) {
    return service.create(body);
  }

  @GetMapping("/{id}")
  public DriverResponse get(@PathVariable UUID id) {
    return service.get(id);
  }
}