package io.smanicome.racemanager.dao;

import io.smanicome.racemanager.core.Runner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record RaceEntity(UUID id, LocalDateTime date, int number, List<Runner> runners) {
}
