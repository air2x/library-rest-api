package ru.maxima.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookResponseDTO {

    private String name;
    private Integer yearOfProduction;
    private String author;
    private String annotation;
    private PersonDTO owner;
}