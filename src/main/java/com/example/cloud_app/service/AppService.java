package com.example.cloud_app.service;

import com.example.cloud_app.model.Person;
import com.example.cloud_app.repo.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppService {

    private final NumberService numberService;
    private final PersonRepository repo;

    public String callRandom() {
        var people = findPeople();
        int id = numberService.getRandomNumber();
        if (id == 0) {
            numberService.failure();
            return null;
        }

        return people.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .map(x -> x.getName())
                .orElseThrow();
    }

    public List<Person> findPeople() {
        var count = repo.count();
        if (count == 0) {
            log.warn("No people found in the database. Initializing with default data...");
            var people = Arrays.asList(
                    Person.builder().id(1).name("Elsa").build(),
                    Person.builder().id(2).name("Vincent van Gogh").build(),
                    Person.builder().id(3).name("Michael Jackson").build()
            );
            repo.saveAll(people);
        }
        return repo.findAll();
    }
}
