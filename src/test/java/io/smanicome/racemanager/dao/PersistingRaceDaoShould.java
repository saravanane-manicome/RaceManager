package io.smanicome.racemanager.dao;

import io.smanicome.racemanager.core.Race;
import io.smanicome.racemanager.core.Runner;
import io.smanicome.racemanager.repository.JpaRaceEntity;
import io.smanicome.racemanager.repository.JpaRunnerEntity;
import io.smanicome.racemanager.repository.RaceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
    void saveRace() {
        final var raceId = UUID.randomUUID();
        final var raceName = "race";
        final var raceNumber = 0;
        final var raceDate = LocalDate.now();

        final var runnerId1 = UUID.randomUUID();
        final var runnerId2 = UUID.randomUUID();
        final var runnerId3 = UUID.randomUUID();
        final var runnerName1 = "runner1";
        final var runnerName2 = "runner2";
        final var runnerName3 = "runner3";
        final var runnerNumber1 = 1;
        final var runnerNumber2 = 2;
        final var runnerNumber3 = 3;

        final var runnersToSave = List.of(
            new Runner(null, runnerName1, runnerNumber1),
            new Runner(null, runnerName2, runnerNumber2),
            new Runner(null, runnerName3, runnerNumber3)
        );
        final var raceToSave = new Race(null, raceName, raceDate, raceNumber, runnersToSave);

        final var jpaRunnerEntities = List.of(
                new JpaRunnerEntity(runnerId1, runnerName1, runnerNumber1, null),
                new JpaRunnerEntity(runnerId2, runnerName2, runnerNumber2, null),
                new JpaRunnerEntity(runnerId3, runnerName3, runnerNumber3, null)
        );
        final var jpaRaceEntity = new JpaRaceEntity(raceId, raceName, raceDate, raceNumber, jpaRunnerEntities);

        final var expectedRunners = List.of(
                new Runner(runnerId1, runnerName1, runnerNumber1),
                new Runner(runnerId2, runnerName2, runnerNumber2),
                new Runner(runnerId3, runnerName3, runnerNumber3)
        );
        final var expectedRace = new Race(raceId, raceName, raceDate, raceNumber, expectedRunners);

        final var jpaRunnerEntitiesToSave = List.of(
                new JpaRunnerEntity(null, runnerName1, runnerNumber1, null),
                new JpaRunnerEntity(null, runnerName2, runnerNumber2, null),
                new JpaRunnerEntity(null, runnerName3, runnerNumber3, null)
        );
        final var jpaRaceEntityToSave = new JpaRaceEntity(null, raceName, raceDate, raceNumber, jpaRunnerEntitiesToSave);

        when(raceRepository.save(any())).thenReturn(jpaRaceEntity);

        final var savedRaceEntity = persistingRaceDao.save(raceToSave);

        assertEquals(expectedRace, savedRaceEntity);

        verify(raceRepository).save(jpaRaceEntityToSave);
        verifyNoMoreInteractions(raceRepository);
    }

    @Test
    @DisplayName("find existing race for given date and number")
    void findRaceForGivenDateAndNumber() {
        final var raceId = UUID.randomUUID();
        final var raceName = "race";
        final var raceNumber = 0;
        final var raceDate = LocalDate.now();

        final var runnerId1 = UUID.randomUUID();
        final var runnerId2 = UUID.randomUUID();
        final var runnerId3 = UUID.randomUUID();
        final var runnerName1 = "runner1";
        final var runnerName2 = "runner2";
        final var runnerName3 = "runner3";
        final var runnerNumber1 = 1;
        final var runnerNumber2 = 2;
        final var runnerNumber3 = 3;
        
        final var runners = List.of(
                new Runner(runnerId1, runnerName1, runnerNumber1),
                new Runner(runnerId2, runnerName2, runnerNumber2),
                new Runner(runnerId3, runnerName3, runnerNumber3)
        );

        final var race = new Race(raceId, raceName, raceDate, raceNumber, runners);

        final var jpaRunnerEntities = List.of(
                new JpaRunnerEntity(runnerId1, runnerName1, runnerNumber1, null),
                new JpaRunnerEntity(runnerId2, runnerName2, runnerNumber2, null),
                new JpaRunnerEntity(runnerId3, runnerName3, runnerNumber3, null)
        );
        final var jpaRaceEntity = new JpaRaceEntity(raceId, raceName, raceDate, raceNumber, jpaRunnerEntities);

        when(raceRepository.findRaceByDateAndNumber(any(), anyInt())).thenReturn(Optional.of(jpaRaceEntity));

        final var result = persistingRaceDao.findRaceByDateAndNumber(raceDate, 0);

        assertEquals(Optional.of(race), result);

        verify(raceRepository).findRaceByDateAndNumber(raceDate, 0);
        verifyNoMoreInteractions(raceRepository);
    }
}