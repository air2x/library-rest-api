package ru.maxima.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.maxima.model.Person;
import ru.maxima.model.enums.Role;
import ru.maxima.repositories.PeopleRepository;

import java.time.LocalDateTime;

@Service
public class RegistrationService {

    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole(Role.ROLE_ADMIN.getName());
        person.setCreatedAt(LocalDateTime.now());
        person.setCreatedPerson(person);
        peopleRepository.save(person);
    }
}
