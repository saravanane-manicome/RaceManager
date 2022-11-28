package io.smanicome.racemanager.core;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record Race(UUID id, LocalDate date, int number, List<Runner> runners) {
}
