package com.ItCareerElevatorFifthExcercise.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebConfig {

    @Value("${messaging-microservice.base-endpoint}")
    private String MESSAGING_MICROSERVICE_BASE_URL;

    @Bean
    public WebClient messagingWebClient() {
        return WebClient
                .builder()
                .baseUrl(MESSAGING_MICROSERVICE_BASE_URL)
                .build();
    }
}
