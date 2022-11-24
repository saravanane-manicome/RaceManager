package io.smanicome.racemanager.service;

import io.smanicome.racemanager.core.Race;
import io.smanicome.racemanager.exceptions.RaceNumberAlreadyUsedForRequestedDateException;
import io.smanicome.racemanager.exceptions.RunnerNumberBreakingSequenceException;

public interface RaceManagerService {
    Race createRace(Race race) throws RaceNumberAlreadyUsedForRequestedDateException, RunnerNumberBreakingSequenceException;
}
