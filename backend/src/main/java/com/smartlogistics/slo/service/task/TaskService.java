package com.smartlogistics.slo.service.task;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartlogistics.slo.api.dto.task.CreateTaskRequest;
import com.smartlogistics.slo.api.dto.task.TaskResponse;
import com.smartlogistics.slo.domain.task.TaskEntity;
import com.smartlogistics.slo.domain.task.TaskRepository;
import com.smartlogistics.slo.service.exception.ConflictException;
import com.smartlogistics.slo.service.exception.NotFoundException;

@Service
public class TaskService {

  private final TaskRepository taskRepository;

  public TaskService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  @Transactional
  public TaskResponse create(CreateTaskRequest req) {
    String ext = req.externalRef() == null ? null : req.externalRef().trim();
    if (ext != null && !ext.isBlank() && taskRepository.existsByExternalRef(ext)) {
      throw new ConflictException(
          "TASK_EXTERNAL_REF_EXISTS",
          "Task with externalRef '" + ext + "' already exists"
      );
    }

    // basic invariant: if both windows present, start <= end
    if (req.windowStart() != null && req.windowEnd() != null && req.windowStart().isAfter(req.windowEnd())) {
      throw new ConflictException("TASK_WINDOW_INVALID", "windowStart must be <= windowEnd");
    }

    Instant now = Instant.now();

    TaskEntity entity = TaskEntity.builder()
        .externalRef(ext != null && ext.isBlank() ? null : ext)
        .customerName(req.customerName().trim())
        .address(req.address().trim())
        .lat(req.lat())
        .lng(req.lng())
        .windowStart(req.windowStart())
        .windowEnd(req.windowEnd())
        .serviceMinutes(req.serviceMinutes())
        .priority(req.priority() == null ? 0 : req.priority())
        .status("PLANNED")
        .createdAt(now)
        .updatedAt(now)
        .build();

    TaskEntity saved = taskRepository.save(entity);
    return toResponse(saved);
  }

  @Transactional(readOnly = true)
  public List<TaskResponse> list() {
    return taskRepository.findAll().stream().map(this::toResponse).toList();
  }

  @Transactional(readOnly = true)
  public TaskResponse get(UUID id) {
    TaskEntity t = taskRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("TASK_NOT_FOUND", "Task '" + id + "' not found"));
    return toResponse(t);
  }

  private TaskResponse toResponse(TaskEntity t) {
    return new TaskResponse(
        t.getId(),
        t.getExternalRef(),
        t.getCustomerName(),
        t.getAddress(),
        t.getLat(),
        t.getLng(),
        t.getWindowStart(),
        t.getWindowEnd(),
        t.getServiceMinutes(),
        t.getPriority(),
        t.getStatus(),
        t.getCreatedAt(),
        t.getUpdatedAt()
    );
  }
}