package ru.maxima.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maxima.dto.PersonRegDTO;
import ru.maxima.model.Person;
import ru.maxima.model.enums.Role;
import ru.maxima.repositories.PeopleRepository;

@Service
public class RegistrationService {

    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    @Autowired
    public RegistrationService(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder, ModelMapper mapper) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    @Transactional
    public void register(PersonRegDTO personRegDto) {
        Person person = mapper.map(personRegDto, Person.class);
        person.setPassword(passwordEncoder.encode(personRegDto.getPassword()));
        person.setRole(Role.ROLE_USER);
        peopleRepository.save(person);
    }
}
