package com.smartlogistics.slo.api.dto.vehicle;

import java.time.Instant;
import java.util.UUID;

public record VehicleResponse(
    UUID id,
    String plateNumber,
    int capacityParcels,
    int capacityKg,
    boolean active,
    Instant createdAt,
    Instant updatedAt
) {}
