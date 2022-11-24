package io.smanicome.racemanager.dao;

import io.smanicome.racemanager.repository.JpaRaceEntity;
import io.smanicome.racemanager.repository.JpaRunnerEntity;
import io.smanicome.racemanager.repository.RaceRepository;

public class PersistingRaceDao implements RaceDao {
    private final RaceRepository raceRepository;

    public PersistingRaceDao(RaceRepository raceRepository) {
        this.raceRepository = raceRepository;
    }

    @Override
    public RaceEntity save(RaceEntity raceEntity) {
        final var jpaRaceEntity = raceRepository.save(raceEntityToJpaRaceEntity(raceEntity));
        return jpaRaceEntityToRaceEntity(jpaRaceEntity);
    }

    private JpaRaceEntity raceEntityToJpaRaceEntity(RaceEntity raceEntity) {
        final var jpaRaceEntity = new JpaRaceEntity();
        jpaRaceEntity.setDate(raceEntity.date());
        jpaRaceEntity.setNumber(raceEntity.number());

        final var jpaRunnerEntities = raceEntity.runners().stream()
            .map(this::runnerEntityToJpaRunnerEntity)
            .toList();

        jpaRaceEntity.setRunners(jpaRunnerEntities);
        return jpaRaceEntity;
    }

    private JpaRunnerEntity runnerEntityToJpaRunnerEntity(RunnerEntity runnerEntity) {
        final var jpaRunnerEntity = new JpaRunnerEntity();
        jpaRunnerEntity.setName(runnerEntity.name());
        jpaRunnerEntity.setNumber(runnerEntity.number());
        return jpaRunnerEntity;
    }

    private RaceEntity jpaRaceEntityToRaceEntity(JpaRaceEntity jpaRaceEntity) {
        final var runnerEntities = jpaRaceEntity.getRunners().stream()
            .map(this::jpaRunnerEntityToRunnerEntity)
            .toList();

        return new RaceEntity(
            jpaRaceEntity.getId(),
            jpaRaceEntity.getDate(),
            jpaRaceEntity.getNumber(),
            runnerEntities
        );
    }

    private RunnerEntity jpaRunnerEntityToRunnerEntity(JpaRunnerEntity jpaRunnerEntity) {
        return new RunnerEntity(jpaRunnerEntity.getId(), jpaRunnerEntity.getName(), jpaRunnerEntity.getNumber());
    }
}
