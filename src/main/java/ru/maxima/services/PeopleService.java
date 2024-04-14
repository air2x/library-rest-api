package ru.maxima.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maxima.dto.PersonDTO;
import ru.maxima.dto.PersonRegDTO;
import ru.maxima.model.*;
import ru.maxima.repositories.PeopleRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;
    private final ModelMapper mapper;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, ModelMapper mapper) {
        this.peopleRepository = peopleRepository;
        this.mapper = mapper;
    }

    @PreAuthorize("hasRole(T(ru.maxima.model.enums.Role).ROLE_ADMIN)")
    public List<PersonDTO> findAllPeople() {
        List<Person> people = peopleRepository.findAll();
        List<PersonDTO> peopleDTO = new ArrayList<>();
        for (Person person : people) {
            if (person.getRemovedAt() != null) {
                continue;
            }
            peopleDTO.add(mapper.map(person, PersonDTO.class));
        }
        return peopleDTO;
    }

    @PreAuthorize("hasRole(T(ru.maxima.model.enums.Role).ROLE_ADMIN)")
    public Person findOnePerson(Long id) {
        Optional<Person> foundPerson = peopleRepository.findById(id);
        return foundPerson.orElse(null);
    }

    @Transactional
    public void savePerson(PersonRegDTO personRegDTO) {
        Person person = mapper.map(personRegDTO, Person.class);
//        person.setCreatedPerson();
        person.setCreatedAt(LocalDateTime.now());
        peopleRepository.save(person);
    }

    @PreAuthorize("hasRole(T(ru.maxima.model.enums.Role).ROLE_ADMIN)")
    @Transactional
    public void updatePerson(Long id, PersonDTO updatePerson) {
        Person person = findOnePerson(id);
        person.setAge(updatePerson.getAge());
        person.setName(updatePerson.getName());
        person.setEmail(updatePerson.getEmail());
        peopleRepository.save(person);
    }

    @PreAuthorize("hasRole(T(ru.maxima.model.enums.Role).ROLE_ADMIN)")
    @Transactional
    public void deletePerson(Long id) {
        Person person = findOnePerson(id);
//        person.setRemovedPerson();
        person.setRemovedAt(LocalDateTime.now());
    }
}
