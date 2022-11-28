package io.smanicome.racemanager.service;

import io.smanicome.racemanager.dao.RaceDao;
import io.smanicome.racemanager.messaging.RacePublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RaceManagerServiceConfiguration {
    @Bean
    public RaceManagerService getRaceManagerService(RaceDao raceDao, RacePublisher racePublisher) {
        return new PublishingRaceManagerService(raceDao, racePublisher);
    }
}
