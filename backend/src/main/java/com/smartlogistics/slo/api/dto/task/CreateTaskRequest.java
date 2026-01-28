package com.smartlogistics.slo.api.dto.task;

import java.time.Instant;

import jakarta.validation.constraints.*;

public record CreateTaskRequest(
    String externalRef,

    @NotBlank String customerName,
    @NotBlank String address,

    @DecimalMin(value = "-90.0") @DecimalMax(value = "90.0")
    double lat,

    @DecimalMin(value = "-180.0") @DecimalMax(value = "180.0")
    double lng,

    Instant windowStart,
    Instant windowEnd,

    @Min(0) int serviceMinutes,
    @Min(0) Integer priority
) {}