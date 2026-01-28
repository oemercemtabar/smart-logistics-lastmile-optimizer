package com.smartlogistics.slo.api.dto.driver;

import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

public record DriverResponse(
    UUID id,
    String fullName,
    String email,
    LocalTime shiftStart,
    LocalTime shiftEnd,
    UUID vehicleId,
    boolean active,
    Instant createdAt,
    Instant updatedAt
) {}