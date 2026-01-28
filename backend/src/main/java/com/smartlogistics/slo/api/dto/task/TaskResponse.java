package com.smartlogistics.slo.api.dto.task;

import java.time.Instant;
import java.util.UUID;

public record TaskResponse(
    UUID id,
    String externalRef,
    String customerName,
    String address,
    double lat,
    double lng,
    Instant windowStart,
    Instant windowEnd,
    int serviceMinutes,
    int priority,
    String status,
    Instant createdAt,
    Instant updatedAt
) {}