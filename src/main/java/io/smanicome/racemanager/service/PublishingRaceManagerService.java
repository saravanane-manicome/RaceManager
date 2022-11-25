package io.smanicome.racemanager.service;

import io.smanicome.racemanager.core.Race;
import io.smanicome.racemanager.core.Runner;
import io.smanicome.racemanager.dao.RaceDao;
import io.smanicome.racemanager.exceptions.RaceNumberAlreadyUsedForRequestedDateException;
import io.smanicome.racemanager.exceptions.RunnerNumberBreakingSequenceException;
import org.springframework.stereotype.Service;

@Service
public class PublishingRaceManagerService implements RaceManagerService {
    private final RaceDao raceDao;

    public PublishingRaceManagerService(RaceDao raceDao) {
        this.raceDao = raceDao;
    }

    @Override
    public Race createRace(Race race) throws RaceNumberAlreadyUsedForRequestedDateException, RunnerNumberBreakingSequenceException {
        assertThatRunnerNumbersAreSequential(race);
        assertThatRaceNumberIsNotUsedForRequestedDate(race);

        return raceDao.save(race);
    }

    private void assertThatRaceNumberIsNotUsedForRequestedDate(Race race) throws RaceNumberAlreadyUsedForRequestedDateException {
        final var raceWithSameDateAndNumber = raceDao.findRaceByDateAndNumber(race.date(), race.number());
        if(raceWithSameDateAndNumber.isPresent()) {
            throw new RaceNumberAlreadyUsedForRequestedDateException();
        }
    }

    private void assertThatRunnerNumbersAreSequential(Race race) throws RunnerNumberBreakingSequenceException {
        final var runnerNumbers = race.runners().stream()
                .map(Runner::number)
                .sorted()
                .toList();

        for (int i = 0; i < runnerNumbers.size(); i++) {
            if(i+1 != runnerNumbers.get(i)) {
                throw new RunnerNumberBreakingSequenceException();
            }
        }
    }
}
