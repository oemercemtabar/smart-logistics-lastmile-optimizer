package com.smartlogistics.slo.api.dto.driver;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record CreateDriverRequest(
    @NotBlank String fullName,
    @Email @NotBlank String email,
    @NotNull LocalTime shiftStart,
    @NotNull LocalTime shiftEnd,
    @NotNull Boolean active
) {}