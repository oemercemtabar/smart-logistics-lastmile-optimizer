package com.smartlogistics.slo.api;

import com.smartlogistics.slo.domain.driver.DriverEntity;
import com.smartlogistics.slo.domain.driver.DriverRepository;
import java.util.UUID;
import java.util.List;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("/api/v1/drivers")
public class DriverController {
    
    private final DriverRepository repo;
    public DriverController(DriverRepository repo){
        this.repo = repo;
    }

    @GetMapping
    public List<DriverEntity> list() { return repo.findAll();}

    @PostMapping
    public DriverEntity create(@RequestBody DriverEntity body) {
        return repo.save(body);
    }

    @GetMapping("/{id}")
    public DriverEntity get(@PathVariable UUID id ) {
        return repo.findById(id).orElseThrow();
    }
    
    
    
}
