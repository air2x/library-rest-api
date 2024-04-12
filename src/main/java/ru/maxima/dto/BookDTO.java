package ru.maxima.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import ru.maxima.model.Person;

@Getter
@Setter
public class BookDTO {

    @NotEmpty(message = "Назавние не должно быть пустым")
    private String name;

    private Integer yearOfProduction;

    @NotEmpty(message = "Автор не должен быть пустым")
    private String author;

    private String annotation;

    private Person owner;

}
