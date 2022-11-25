package io.smanicome.racemanager.api.rest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record RaceResponse(UUID id, LocalDateTime date, int number, List<RunnerResponse> runners) {
}
