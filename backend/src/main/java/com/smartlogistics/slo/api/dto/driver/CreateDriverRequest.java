package com.smartlogistics.slo.api.dto.driver;

import java.time.LocalTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateDriverRequest(
    @NotBlank String fullName,
    @NotBlank @Email String email,
    @NotNull LocalTime shiftStart,
    @NotNull LocalTime shiftEnd,
    Boolean active
) {}