package ru.maxima.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Book {

    @Id
    private Long id;

    private String name;

    private Integer yearOfProduction;

    private String author;

    private String annotation;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime removedAt;

    private String createdPerson;

    private String updatedPerson;

    private String removedPerson;
}
