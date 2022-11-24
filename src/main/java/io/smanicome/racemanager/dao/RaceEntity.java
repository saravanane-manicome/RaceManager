package io.smanicome.racemanager.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record RaceEntity(UUID id, LocalDateTime date, int number, List<RunnerEntity> runners) {
}
