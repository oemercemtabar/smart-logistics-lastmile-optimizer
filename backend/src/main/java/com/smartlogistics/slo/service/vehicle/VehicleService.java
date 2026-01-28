package com.smartlogistics.slo.service.vehicle;

import com.smartlogistics.slo.api.dto.vehicle.CreateVehicleRequest;
import com.smartlogistics.slo.api.dto.vehicle.VehicleResponse;
import com.smartlogistics.slo.domain.vehicle.VehicleEntity;
import com.smartlogistics.slo.domain.vehicle.VehicleRepository;

import java.util.List;
import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class VehicleService {
    private final VehicleRepository repo;

    public VehicleService(VehicleRepository repo){
         this.repo = repo;
    }

    @Transactional
    public VehicleResponse create(CreateVehicleRequest req){
        var entity = VehicleEntity.builder()
            .plateNumber(req.plateNumber())
            .capacityParcels(req.capacityParcels())
            .capacityKg(req.capacityKg())
            .active(Boolean.TRUE.equals(req.active()))
            .build();
        
            try {
                entity = repo.save(entity);
            } catch (DataIntegrityViolationException e) {
                throw new VehicleAlreadyExistsException(req.plateNumber(), e);
            }

            return toResponse(entity);
    }

    @Transactional(readOnly = true)
    public VehicleResponse get(UUID id){
        var entity = repo.findById(id).orElseThrow(() -> new VehicleNotFoundException(id));
        return toResponse(entity);
    }

    @Transactional(readOnly = true)
    public List<VehicleResponse> list(){
        return repo.findAll().stream().map(this::toResponse).toList();
    }

    private VehicleResponse toResponse(VehicleEntity v){
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
