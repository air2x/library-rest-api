package ru.maxima.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Person {

    @Id
    private Long id;

    private String name;

    private Integer age;

    private String email;

    private String phoneNumber;

    private String password;

    private String role;

    private LocalDateTime createdAt;

    private LocalDateTime removedAt;

    private String createdPerson;

    private String removedPerson;
}
