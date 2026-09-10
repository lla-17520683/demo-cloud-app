package com.example.cloud_app.repo;

import com.example.cloud_app.model.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PersonRepository extends MongoRepository<Person, Integer> {
}
