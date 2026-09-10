package com.example.cloud_app.controller;

import com.example.cloud_app.model.Person;
import com.example.cloud_app.service.NumberService;
import com.example.cloud_app.repo.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final NumberService numberService;
    private final PersonRepository personRepository;

    @GetMapping
    public ResponseEntity<?> ok() {
        var health = numberService.getHealth();
        return ResponseEntity.ok("Everything is so good! " + health);
    }

    @GetMapping("random")
    public ResponseEntity<?> randomException() {
        int id = numberService.getRandomNumber();
        var person = personRepository.findById(id)
                .map(x -> x.getName())
                .orElseThrow();
        return ResponseEntity.ok(person);
    }

    @GetMapping("person")
    public ResponseEntity<?> getAllPersons() {
        return ResponseEntity.ok(personRepository.findAll());
    }

    @PostMapping("save")
    public ResponseEntity<?> savePersons() {
        if (personRepository.count() > 0) {
            return ResponseEntity.ok("Exist persons. No save!!!");
        }

        List<String> names = numberService.getPersons();
        if (names.isEmpty()) {
            return ResponseEntity.ok("No persons received. No save!!!");
        }

        int i = 0;
        for (var name : names) {
            Person person = new Person(++i, name);
            personRepository.save(person);
        }
        return ResponseEntity.ok("Saved " + names.size() + " persons successfully");
    }
}
