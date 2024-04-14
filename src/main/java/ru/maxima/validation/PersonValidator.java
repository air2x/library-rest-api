package ru.maxima.validation;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.maxima.model.Person;
import ru.maxima.services.PersonDetailsService;

@Component
public class PersonValidator implements Validator {

    private final PersonDetailsService service;
    private final ModelMapper mapper;

    @Autowired
    public PersonValidator(PersonDetailsService service, ModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person p = mapper.map(target, Person.class);

        try {
            service.loadUserByUsername(p.getEmail());
        } catch (UsernameNotFoundException e) {
            return;
        }

        errors.rejectValue("username", "100", "User with this email existed");
    }
}
