package ru.maxima.util.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.maxima.model.Person;
import ru.maxima.services.PersonDetailsService;
import ru.maxima.util.exeptions.PersonNotFoundException;

@Component
public class PersonValidator implements Validator {

    private final PersonDetailsService service;

    @Autowired
    public PersonValidator(PersonDetailsService service) {
        this.service = service;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person p = (Person) target;

        try {
            service.loadUserByUsername(p.getEmail());
        } catch (PersonNotFoundException e) {
            return;
        }
        errors.rejectValue("email", "100", "User with this email existed");
    }
}
