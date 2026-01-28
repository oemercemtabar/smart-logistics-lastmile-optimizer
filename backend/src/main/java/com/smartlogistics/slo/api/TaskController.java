package com.smartlogistics.slo.api;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.smartlogistics.slo.api.dto.task.CreateTaskRequest;
import com.smartlogistics.slo.api.dto.task.TaskResponse;
import com.smartlogistics.slo.service.task.TaskService;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @PostMapping
  public ResponseEntity<TaskResponse> create(@Valid @RequestBody CreateTaskRequest req) {
    return ResponseEntity.status(201).body(taskService.create(req));
  }

  @GetMapping
  public ResponseEntity<List<TaskResponse>> list() {
    return ResponseEntity.ok(taskService.list());
  }

  @GetMapping("/{taskId}")
  public ResponseEntity<TaskResponse> get(@PathVariable UUID taskId) {
    return ResponseEntity.ok(taskService.get(taskId));
  }
}