package com.example.cloud_app.service;

import com.example.cloud_app.model.Person;
import com.example.cloud_app.repo.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NumberService {

    private final RestClient restClient;
    private final PersonRepository repo;

    @Value("${api.number-service-url}")
    private String baseUrl;

    public String getHealth() {
        log.info("Calling number-service health check at {}", baseUrl + "/actuator/health");
        try {
            return restClient.get()
                    .uri(baseUrl + "/actuator/health")
//                    .uri(baseUrl + "/actuator/health/liveness")
//                    .uri(baseUrl + "/actuator/health/readiness")
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
//            log.error(e.getMessage());
            throw new RuntimeException("Failed to call number-service random endpoint", e);
        }
    }

    public void failure() {
        restClient.get()
                .uri(baseUrl + "/divide")
                .retrieve()
                .body(Double.class);
    }

    public List<Person> findPeople() {
        var people = repo.findAll();
        if (people.isEmpty()) {
            log.warn("No USER found in the database.");
            people = Arrays.asList(
                    Person.builder().id(1).name("Elsa").build(),
                    Person.builder().id(2).name("Vincent van Gogh").build(),
                    Person.builder().id(3).name("Michael Jackson").build()
            );
            repo.saveAll(people);
        }
        return people;
    }
}
