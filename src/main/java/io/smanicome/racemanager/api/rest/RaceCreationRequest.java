package io.smanicome.racemanager.api.rest;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

public record RaceCreationRequest(@NotNull LocalDateTime date, @Min(1) int number, @NotNull @Size(min = 3)
                                List<@Valid RunnerCreationRequest> runners) {
}
