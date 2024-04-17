package ru.maxima.dto;

import lombok.Getter;
import lombok.Setter;
import ru.maxima.model.enums.Role;

@Getter
@Setter
public class PersonRegDTO {

    private String name;
    private Integer age;
    private String email;
    private String phoneNumber;
    private String password;
    private Role role;
}
