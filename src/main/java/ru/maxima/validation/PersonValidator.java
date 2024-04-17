package ru.maxima.validation;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.maxima.model.Person;
import ru.maxima.services.PersonDetailsService;
import ru.maxima.util.Exeptions.PersonNotFoundException;

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
            service.loadUserByUsername(p.getName());
        } catch (PersonNotFoundException e) {
            return;
        }
        errors.rejectValue("name" , "100" , "User with this nickName existed");
    }
}
