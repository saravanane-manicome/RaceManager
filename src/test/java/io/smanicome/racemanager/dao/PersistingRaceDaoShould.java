package io.smanicome.racemanager.dao;

import io.smanicome.racemanager.repository.JpaRaceEntity;
import io.smanicome.racemanager.repository.JpaRunnerEntity;
import io.smanicome.racemanager.repository.RaceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersistingRaceDaoShould {
    @Mock
    private RaceRepository raceRepository;

    @InjectMocks
    private PersistingRaceDao persistingRaceDao;

    @Test
    @DisplayName("save a race")
    void shouldSaveRace() {
        final var raceId = UUID.randomUUID();
        final var runnerId1 = UUID.randomUUID();
        final var runnerId2 = UUID.randomUUID();
        final var runnerId3 = UUID.randomUUID();

        final var runnerEntitiesToSave = List.of(
            new RunnerEntity(null, "runner1", 1),
            new RunnerEntity(null, "runner2", 2),
            new RunnerEntity(null, "runner3", 3)
        );
        final var raceEntityToSave = new RaceEntity(null, LocalDateTime.now(), 0, runnerEntitiesToSave);

        final var jpaRunnerEntities = List.of(
                new JpaRunnerEntity(runnerId1, "runner1", 1, null),
                new JpaRunnerEntity(runnerId2, "runner2", 2, null),
                new JpaRunnerEntity(runnerId3, "runner3", 3, null)
        );
        final var jpaRaceEntity = new JpaRaceEntity(raceId, raceEntityToSave.date(), raceEntityToSave.number(), jpaRunnerEntities);

        when(raceRepository.save(any())).thenReturn(jpaRaceEntity);

        final var savedRaceEntity = persistingRaceDao.save(raceEntityToSave);

        final var expectedRunnerEntities = List.of(
                new RunnerEntity(runnerId1, "runner1", 1),
                new RunnerEntity(runnerId2, "runner2", 2),
                new RunnerEntity(runnerId3, "runner3", 3)
        );
        final var expectedRaceEntity = new RaceEntity(raceId, raceEntityToSave.date(), 0, expectedRunnerEntities);
        assertEquals(expectedRaceEntity, savedRaceEntity);

        final var jpaRunnerEntitiesToSave = List.of(
                new JpaRunnerEntity(null, "runner1", 1, null),
                new JpaRunnerEntity(null, "runner2", 2, null),
                new JpaRunnerEntity(null, "runner3", 3, null)
        );
        verify(raceRepository).save(new JpaRaceEntity(null, raceEntityToSave.date(), raceEntityToSave.number(), jpaRunnerEntitiesToSave));
        verifyNoMoreInteractions(raceRepository);
    }
}