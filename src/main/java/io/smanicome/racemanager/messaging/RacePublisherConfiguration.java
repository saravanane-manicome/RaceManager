package io.smanicome.racemanager.messaging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RacePublisherConfiguration {
    @Bean
    public RacePublisher getRacePublisher() {
        return new KafkaRacePublisher();
    }
}
