package com.smartlogistics.slo.api;

import com.smartlogistics.slo.domain.vehicle.VehicleEntity;
import com.smartlogistics.slo.domain.vehicle.VehicleRepository;
import java.util.UUID;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/v1/vehicles")
public class VehicleController {
    private final VehicleRepository repo;
    public VehicleController(VehicleRepository repo) { this.repo = repo;}

    @GetMapping
    public List<VehicleEntity> list() {
        return repo.findAll();
    }
    
    @PostMapping
    public VehicleEntity create(@RequestBody VehicleEntity body) {
        return repo.save(body);
    }

    @GetMapping("/{id}")
    public VehicleEntity get(@PathVariable UUID id) {
        return repo.findById(id).orElseThrow();
    }
    
}
