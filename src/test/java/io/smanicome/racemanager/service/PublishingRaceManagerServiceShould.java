package io.smanicome.racemanager.service;

import io.smanicome.racemanager.core.Race;
import io.smanicome.racemanager.core.Runner;
import io.smanicome.racemanager.dao.RaceDao;
import io.smanicome.racemanager.exceptions.RaceNumberAlreadyUsedForRequestedDateException;
import io.smanicome.racemanager.exceptions.RunnerNumberBreakingSequenceException;
import io.smanicome.racemanager.messaging.RacePublisher;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublishingRaceManagerServiceShould {
    @Mock
    private RaceDao raceDao;

    @Mock
    private RacePublisher racePublisher;

    @InjectMocks
    private PublishingRaceManagerService publishingRaceManagerService;

    @Test
    @DisplayName("create a race")
    void createRace() throws RunnerNumberBreakingSequenceException, RaceNumberAlreadyUsedForRequestedDateException {
        final var raceId = UUID.randomUUID();
        final var runnerId1 = UUID.randomUUID();
        final var runnerId2 = UUID.randomUUID();
        final var runnerId3 = UUID.randomUUID();

        final var date = LocalDate.now();
        final var number = 0;
        final var runnersToSave = List.of(
                new Runner(null, "runner 1", 1),
                new Runner(null, "runner 2", 2),
                new Runner(null, "runner 3", 3)
        );
        final var raceToSave = new Race(null, date, number, runnersToSave);

        final var expectedRunners = List.of(
                new Runner(runnerId1, "runner 1", 1),
                new Runner(runnerId2, "runner 2", 2),
                new Runner(runnerId3, "runner 3", 3)
        );
        final var expectedRace = new Race(raceId, date, number, expectedRunners);

        when(raceDao.save(any())).thenReturn(expectedRace);

        final var result = publishingRaceManagerService.createRace(raceToSave);

        assertEquals(expectedRace, result);

        final var orderVerifier = inOrder(raceDao, racePublisher);
        orderVerifier.verify(raceDao).findRaceByDateAndNumber(date, number);
        orderVerifier.verify(raceDao).save(raceToSave);
        orderVerifier.verify(racePublisher).publishCreation(expectedRace);
        orderVerifier.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("throw if runner numbers are not sequential")
    void throwIfRunnerNumbersAreNotSequential() {
        final var runnersToSave = List.of(
                new Runner(null, "runner 1", 1),
                new Runner(null, "runner 2", 2),
                new Runner(null, "runner 4", 4)
        );
        final var raceToSave = new Race(null, LocalDate.now(), 0, runnersToSave);

        assertThrows(RunnerNumberBreakingSequenceException.class, () -> publishingRaceManagerService.createRace(raceToSave));

        verifyNoInteractions(raceDao);
    }

    @Test
    @DisplayName("throw if runner numbers are not sequential")
    void throwIfRaceNumberIsAlreadyUsedForRequestedDate() {
        final var date = LocalDate.now();
        final var number = 0;

        final var runnersToSave = List.of(
                new Runner(null, "runner 1", 1),
                new Runner(null, "runner 2", 2),
                new Runner(null, "runner 3", 3)
        );
        final var raceToSave = new Race(null, date, number, runnersToSave);

        when(raceDao.findRaceByDateAndNumber(any(), anyInt())).thenReturn(Optional.of(raceToSave));

        assertThrows(RaceNumberAlreadyUsedForRequestedDateException.class, () -> publishingRaceManagerService.createRace(raceToSave));

        verify(raceDao).findRaceByDateAndNumber(date, number);
        verifyNoMoreInteractions(raceDao);
    }
}