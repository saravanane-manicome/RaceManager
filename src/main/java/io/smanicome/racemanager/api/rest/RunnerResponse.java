package io.smanicome.racemanager.api.rest;

import java.util.UUID;

public record RunnerResponse(UUID id, String name, int number) {
}
