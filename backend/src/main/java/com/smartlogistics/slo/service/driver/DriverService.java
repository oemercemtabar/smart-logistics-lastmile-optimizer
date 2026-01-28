package com.smartlogistics.slo.service.driver;

import com.smartlogistics.slo.api.dto.driver.CreateDriverRequest;
import com.smartlogistics.slo.api.dto.driver.DriverResponse;
import com.smartlogistics.slo.domain.driver.DriverEntity;
import com.smartlogistics.slo.domain.driver.DriverRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DriverService {
  private final DriverRepository repo;

  public DriverService(DriverRepository repo) {
    this.repo = repo;
  }

  @Transactional
  public DriverResponse create(CreateDriverRequest req) {
    var entity = DriverEntity.builder()
        .fullName(req.fullName())
        .email(req.email())
        .shiftStart(req.shiftStart())
        .shiftEnd(req.shiftEnd())
        .active(Boolean.TRUE.equals(req.active()))
        .build();

    try {
      entity = repo.save(entity);
    } catch (DataIntegrityViolationException e) {
      throw new DriverAlreadyExistsException(req.email(), e);
    }

    return toResponse(entity);
  }

  @Transactional(readOnly = true)
  public DriverResponse get(UUID id) {
    var entity = repo.findById(id).orElseThrow(() -> new DriverNotFoundException(id));
    return toResponse(entity);
  }

  @Transactional(readOnly = true)
  public List<DriverResponse> list() {
    return repo.findAll().stream().map(this::toResponse).toList();
  }

  private DriverResponse toResponse(DriverEntity d) {
    return new DriverResponse(
        d.getId(),
        d.getFullName(),
        d.getEmail(),
        d.getShiftStart(),
        d.getShiftEnd(),
        d.getVehicle() == null ? null : d.getVehicle().getId(),
        d.isActive(),
        d.getCreatedAt(),
        d.getUpdatedAt()
    );
  }
}