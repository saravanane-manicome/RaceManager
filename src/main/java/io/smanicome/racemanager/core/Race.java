package io.smanicome.racemanager.core;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record Race(UUID id, LocalDateTime date, int number, List<Runner> runners) {
}
