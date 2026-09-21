package com.example.cloud_app.controller;

import com.example.cloud_app.service.AppService;
import com.example.cloud_app.service.NumberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final NumberService numberService;
    private final AppService appService;

    @GetMapping
    public ResponseEntity<?> good() {
        return ResponseEntity.ok("good good");
    }

    @GetMapping("api1")
    public ResponseEntity<?> ok() {
        var health = numberService.getHealth();
        return ResponseEntity.ok("Everything is so good! " + health);
    }

    @GetMapping("api2")
    public ResponseEntity<?> randomException() {
        return ResponseEntity.ok(appService.callRandom());
    }

    @GetMapping("api3")
    public ResponseEntity<?> failure() {
        numberService.failure();
        return ResponseEntity.ok("This should never be reached");
    }

    @GetMapping("people")
    public ResponseEntity<?> getAllPersons() {
        return ResponseEntity.ok(appService.findPeople());
    }
}
