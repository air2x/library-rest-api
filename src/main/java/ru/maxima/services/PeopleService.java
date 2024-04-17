package ru.maxima.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maxima.dto.BookDTO;
import ru.maxima.dto.PersonDTO;
import ru.maxima.model.*;
import ru.maxima.model.enums.Role;
import ru.maxima.repositories.PeopleRepository;
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
    private final BooksService booksService;


    @Autowired
    public PeopleService(PeopleRepository peopleRepository, ModelMapper mapper, BooksService booksService) {
        this.peopleRepository = peopleRepository;
        this.mapper = mapper;
        this.booksService = booksService;
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
        return foundPerson.orElseThrow(PersonNotFoundException::new);
    }

    public Person findByName(String name) {
        Optional<Person> foundPerson = peopleRepository.findByName(name);
        return foundPerson.orElseThrow(PersonNotFoundException::new);
    }

    public Person findByEmail(String email) {
        Optional<Person> foundPerson = peopleRepository.findByEmail(email);
        return foundPerson.orElseThrow(PersonNotFoundException::new);
    }

    @Transactional
    public void savePerson(Person person) {
//        Person person = mapper.map(person, Person.class);
//        person.setCreatedPerson();
        person.setCreatedAt(LocalDateTime.now());
        person.setRole(Role.ROLE_USER);
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

    @Transactional
    public void takeBook(Long personId, BookDTO bookDTO) {
        Book book = booksService.findOneBookByName(bookDTO.getName());
        Person person = findOnePerson(personId);
        List<Book> personBooks = person.getBooks();
        personBooks.add(book);
        person.setBooks(personBooks);
    }
}
