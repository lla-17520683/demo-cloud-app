package com.example.cloud_app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Service
@RequiredArgsConstructor
@Slf4j
public class NumberService {

    private final RestClient restClient;

    @Value("${api.number-service-url}")
    private String baseUrl;

    public String getHealth() {
        log.info("Calling number-service health check at {}", baseUrl + "/actuator/health");
        try {
            return restClient.get()
                    .uri(baseUrl + "/actuator/health")
                    .retrieve()
                    .body(String.class);
        }
        catch (RestClientResponseException e) {
            log.error(e.getMessage());
            throw new RuntimeException("Failed to call number-service health check", e);
        }
    }

    public Integer getRandomNumber() {
        try {
            return restClient.get()
                    .uri(baseUrl + "/random")
                    .retrieve()
                    .body(Integer.class);
        }
        catch (RestClientResponseException e) {
            throw new RuntimeException("Failed to call random endpoint", e);
        }
    }

    public void failure() {
        restClient.get()
                .uri(baseUrl + "/divide")
                .retrieve()
                .body(Double.class);
    }
}
