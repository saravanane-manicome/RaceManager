package io.smanicome.racemanager.api.rest;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public record RunnerCreationRequest(@NotBlank String name, @Min(1) int number) {
}
