package com.example.cloud_app.controller;

import com.example.cloud_app.service.NumberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final NumberService numberService;

    @GetMapping
    public ResponseEntity<?> ok() {
        var health = numberService.getHealth();
        return ResponseEntity.ok("Everything is so good! " + health);
    }

    @GetMapping("random")
    public ResponseEntity<?> randomException() {
        int id = numberService.getRandomNumber();
        if (id == 0) {
            numberService.failure();
        }
        return ResponseEntity.ok(id);
    }

    @GetMapping("failure")
    public ResponseEntity<?> failure() {
        numberService.failure();
        return ResponseEntity.ok("This should never be reached");
    }

    @GetMapping("people")
    public ResponseEntity<?> getAllPersons() {
        return ResponseEntity.ok(numberService.findPeople());
    }
}
