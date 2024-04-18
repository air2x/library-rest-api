package ru.maxima.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maxima.dto.BookDTO;
import ru.maxima.dto.PersonDTO;
import ru.maxima.model.*;
import ru.maxima.model.enums.Role;
import ru.maxima.repositories.PeopleRepository;
import ru.maxima.security.PersonDetails;
import ru.maxima.util.Exeptions.PersonNotFoundException;

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

    @PreAuthorize("hasAuthority(T(ru.maxima.model.enums.Role).ADMIN.getName())")
    public Person findOnePerson(Long id) {
        Optional<Person> foundPerson = peopleRepository.findById(id);
        return foundPerson.orElseThrow(PersonNotFoundException::new);
    }

    public Person findByEmail(String email) {
        Optional<Person> foundPerson = peopleRepository.findByEmail(email);
        return foundPerson.orElseThrow(PersonNotFoundException::new);
    }

    @PreAuthorize("hasAuthority(T(ru.maxima.model.enums.Role).ADMIN.getName())")
    public void savePerson(Person person, PersonDetails userDetails) {
        Person personDetails = findByEmail(userDetails.getUsername());
        person.setCreatedPerson(personDetails);
        person.setCreatedAt(LocalDateTime.now());
        person.setRole(Role.USER.getName());
        peopleRepository.save(person);
    }

    @PreAuthorize("hasAuthority(T(ru.maxima.model.enums.Role).ADMIN.getName())")
    @Transactional
    public void deletePerson(Long id, PersonDetails personDetails) {
        Person person = findOnePerson(id);
        person.setRemovedPerson(findByEmail(personDetails.getUsername()));
        person.setRemovedAt(LocalDateTime.now());
    }

    public Person getMyPerson(PersonDetails personDetails) {
        return peopleRepository.findByEmail(personDetails.getUsername()).orElseThrow(PersonNotFoundException::new);
    }

    public List<BookDTO> getMyBooks(PersonDetails personDetails) {
        Person person = peopleRepository.findByEmail(personDetails.getUsername()).orElseThrow(PersonNotFoundException::new);
        List<BookDTO> booksDTO = new ArrayList<>();
        List<Book> books = person.getBooks();
        books.forEach(book -> booksDTO.add(mapper.map(book, BookDTO.class)));
        return booksDTO;
    }
}
