package io.smanicome.racemanager.api.rest;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record RaceResponse(UUID id, LocalDate date, int number, List<RunnerResponse> runners) {
}
