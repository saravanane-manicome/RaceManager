package io.smanicome.racemanager.dao;

import io.smanicome.racemanager.core.Race;
import io.smanicome.racemanager.core.Runner;
import io.smanicome.racemanager.repository.JpaRaceEntity;
import io.smanicome.racemanager.repository.JpaRunnerEntity;
import io.smanicome.racemanager.repository.RaceRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class PersistingRaceDao implements RaceDao {
    private final RaceRepository raceRepository;

    public PersistingRaceDao(RaceRepository raceRepository) {
        this.raceRepository = raceRepository;
    }

    @Override
    public Race save(Race raceEntity) {
        final var jpaRaceEntity = raceRepository.save(raceToJpaRaceEntity(raceEntity));
        return jpaRaceEntityToRace(jpaRaceEntity);
    }

    @Override
    public Optional<Race> findRaceByDateAndNumber(LocalDateTime date, int number) {
        return raceRepository.findRaceByDateAndNumber(date, number).map(this::jpaRaceEntityToRace);
    }

    private JpaRaceEntity raceToJpaRaceEntity(Race raceEntity) {
        final var jpaRaceEntity = new JpaRaceEntity();
        jpaRaceEntity.setDate(raceEntity.date());
        jpaRaceEntity.setNumber(raceEntity.number());

        final var jpaRunnerEntities = raceEntity.runners().stream()
            .map(this::runnerToJpaRunnerEntity)
            .toList();

        jpaRaceEntity.setRunners(jpaRunnerEntities);
        return jpaRaceEntity;
    }

    private JpaRunnerEntity runnerToJpaRunnerEntity(Runner runnerEntity) {
        final var jpaRunnerEntity = new JpaRunnerEntity();
        jpaRunnerEntity.setName(runnerEntity.name());
        jpaRunnerEntity.setNumber(runnerEntity.number());
        return jpaRunnerEntity;
    }

    private Race jpaRaceEntityToRace(JpaRaceEntity jpaRaceEntity) {
        final var runnerEntities = jpaRaceEntity.getRunners().stream()
            .map(this::jpaRunnerEntityToRunnerEntity)
            .toList();

        return new Race(
            jpaRaceEntity.getId(),
            jpaRaceEntity.getDate(),
            jpaRaceEntity.getNumber(),
            runnerEntities
        );
    }

    private Runner jpaRunnerEntityToRunnerEntity(JpaRunnerEntity jpaRunnerEntity) {
        return new Runner(jpaRunnerEntity.getId(), jpaRunnerEntity.getName(), jpaRunnerEntity.getNumber());
    }
}
