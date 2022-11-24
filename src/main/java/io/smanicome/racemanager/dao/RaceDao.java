package io.smanicome.racemanager.dao;


import io.smanicome.racemanager.core.Race;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RaceDao {
    Race save(Race raceEntity);
    Optional<Race> findRaceByDateAndNumber(LocalDateTime date, int number);
}
