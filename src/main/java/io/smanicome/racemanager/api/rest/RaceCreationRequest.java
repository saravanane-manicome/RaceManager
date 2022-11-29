package io.smanicome.racemanager.api.rest;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

public record RaceCreationRequest(@NotBlank String name, @NotNull LocalDate date, @Min(1) int number,
                                  @NotNull @Size(min = 3) List<@Valid RunnerCreationRequest> runners) {
}
