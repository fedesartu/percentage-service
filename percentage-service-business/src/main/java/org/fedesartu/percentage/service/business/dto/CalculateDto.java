package org.fedesartu.percentage.service.business.dto;

import jakarta.validation.constraints.NotNull;

public record CalculateDto(@NotNull Integer numOne, @NotNull Integer numTwo) {
}
