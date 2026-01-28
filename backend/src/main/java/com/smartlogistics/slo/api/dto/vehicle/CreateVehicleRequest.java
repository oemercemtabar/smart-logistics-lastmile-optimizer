package com.smartlogistics.slo.api.dto.vehicle;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record CreateVehicleRequest(
    @NotBlank String plateNumber,
    @Min(1) int capacityParcels,
    @Min(1) int capacityKg,
    @NotNull Boolean active
) {}