package io.smanicome.racemanager.dao;

import java.util.UUID;

public record RunnerEntity(UUID id, String name, int number) {
}
