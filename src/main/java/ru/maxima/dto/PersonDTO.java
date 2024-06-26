package ru.maxima.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonDTO {

    private String name;
    private Integer age;

    @Email
    private String email;
    private String phoneNumber;

}
