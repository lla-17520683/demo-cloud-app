package com.example.cloud_app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "persons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    private Integer id;
    private String name;
}
