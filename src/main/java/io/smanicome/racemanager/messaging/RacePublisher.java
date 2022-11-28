package io.smanicome.racemanager.messaging;

import io.smanicome.racemanager.core.Race;


public interface RacePublisher {
    void publishCreation(Race race);
}
