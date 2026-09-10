package com.example.cloud_app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NumberService {

    private final RestClient restClient;

    @Value("${api.number-service-url}")
    private String baseUrl;

    public String getHealth() {
        try {
            return restClient.get()
                    .uri(baseUrl + "/actuator/health")
//                    .uri(baseUrl + "/actuator/health/liveness")
//                    .uri(baseUrl + "/actuator/health/readiness")
                    .retrieve()
                    .body(String.class);
        } catch (RestClientResponseException e) {
            log.error(e.getMessage());
            throw new RuntimeException("Failed to call number-service health check", e);
        }
    }

    public Integer getRandomNumber() {
        try {
            return restClient.get()
                    .uri(baseUrl + "/api/random")
                    .retrieve()
                    .body(Integer.class);
        } catch (RestClientResponseException e) {
//            log.error(e.getMessage());
            throw new RuntimeException("Failed to call number-service random endpoint", e);
        }
    }

    public List<String> getPersons() {
        try {
            return restClient.get()
                    .uri(baseUrl + "/api/person")
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<String>>() {});
        } catch (Exception e) {
            log.error("Failed to call number-service person endpoint: {}", e.getMessage());
            return List.of();
        }
    }
}
