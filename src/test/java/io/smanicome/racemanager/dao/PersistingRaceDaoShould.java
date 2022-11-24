package io.smanicome.racemanager.dao;

import io.smanicome.racemanager.repository.JpaRaceEntity;
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
        final var id = UUID.randomUUID();
        final var date = LocalDateTime.now();

        final var raceEntityToSave = new RaceEntity(null, date, 0, List.of());
        final var jpaRaceEntity = new JpaRaceEntity(id, date, raceEntityToSave.number(), List.of());

        when(raceRepository.save(any())).thenReturn(jpaRaceEntity);

        final var savedRaceEntity = persistingRaceDao.save(raceEntityToSave);

        final var expectedRaceEntity = new RaceEntity(id, date, 0, List.of());
        assertEquals(expectedRaceEntity, savedRaceEntity);

        verify(raceRepository).save(new JpaRaceEntity(null, date, raceEntityToSave.number(), List.of()));
        verifyNoMoreInteractions(raceRepository);
    }
}