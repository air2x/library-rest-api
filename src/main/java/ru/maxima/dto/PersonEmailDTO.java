package ru.maxima.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonEmailDTO {

    @Email
    private String email;
}
