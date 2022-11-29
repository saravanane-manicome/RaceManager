package io.smanicome.racemanager.api.rest;

import io.smanicome.racemanager.core.Race;
import io.smanicome.racemanager.core.Runner;
import io.smanicome.racemanager.exceptions.RaceNumberAlreadyUsedForRequestedDateException;
import io.smanicome.racemanager.exceptions.RunnerNumberBreakingSequenceException;
import io.smanicome.racemanager.service.RaceManagerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/races")
public class RaceController {
    private final RaceManagerService raceManagerService;

    public RaceController(RaceManagerService raceManagerService) {
        this.raceManagerService = raceManagerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RaceResponse createRace(@RequestBody @Valid RaceCreationRequest raceCreationRequest) throws RunnerNumberBreakingSequenceException, RaceNumberAlreadyUsedForRequestedDateException {
        final var raceToSave = raceRequestToRace(raceCreationRequest);
        final var savedRace = raceManagerService.createRace(raceToSave);
        return raceToRaceResponse(savedRace);
    }


    private Race raceRequestToRace(RaceCreationRequest raceCreationRequest) {
        final var runners = raceCreationRequest.runners().stream()
                .map(createRunnerRequest -> new Runner(
                        null,
                        createRunnerRequest.name(),
                        createRunnerRequest.number()
                ))
                .toList();
        return new Race(null, raceCreationRequest.name(), raceCreationRequest.date(), raceCreationRequest.number(), runners);
    }

    private RaceResponse raceToRaceResponse(Race race) {
        final var runners = race.runners().stream()
            .map(
                runner -> new RunnerResponse(
                    runner.id(),
                    runner.name(),
                    runner.number()
                )
            )
            .toList();
        return new RaceResponse(race.id(), race.name(), race.date(), race.number(), runners);
    }
}
