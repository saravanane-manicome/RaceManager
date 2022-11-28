package io.smanicome.racemanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface RaceRepository extends JpaRepository<JpaRaceEntity, UUID> {
    Optional<JpaRaceEntity> findRaceByDateAndNumber(LocalDate date, int number);
}
