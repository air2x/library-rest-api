package ru.maxima.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDTO {

    private String name;
    private Integer yearOfProduction;
    private String author;
    private String annotation;
}
