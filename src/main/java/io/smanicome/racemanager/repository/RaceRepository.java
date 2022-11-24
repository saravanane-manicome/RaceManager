package io.smanicome.racemanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RaceRepository extends JpaRepository<JpaRaceEntity, UUID> {
}
