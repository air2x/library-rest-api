package ru.maxima.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.maxima.dto.BookDTO;
import ru.maxima.dto.PersonDTO;
import ru.maxima.dto.PersonRegDTO;
import ru.maxima.model.Book;
import ru.maxima.model.Person;
import ru.maxima.services.BooksService;
import ru.maxima.services.PeopleService;

import java.util.List;

@RestController
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;
    private final BooksService booksService;
    private final ModelMapper mapper;

    @Autowired
    public PeopleController(PeopleService peopleService, BooksService booksService, ModelMapper mapper) {
        this.peopleService = peopleService;
        this.booksService = booksService;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<PersonDTO>> showAllPeople() {
        return ResponseEntity.ok(peopleService.findAllPeople());
    }

    @GetMapping("/{id}")
    public PersonDTO showPerson(@PathVariable("id") Long id) {
        Person person = peopleService.findOnePerson(id);
        return mapper.map(person, PersonDTO.class);
    }

    @PostMapping("/{id}/takeBook")
    public ResponseEntity<HttpStatus> takeBook(@PathVariable("id") Long id,
                                               BookDTO bookDTO) {
        peopleService.takeBook(id, bookDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/createPerson")
    public ResponseEntity<HttpStatus> createPerson(@RequestBody @Valid PersonRegDTO personRegDTO,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RuntimeException("Error create person");
        }
        peopleService.savePerson(personRegDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/updatePerson")
    public ResponseEntity<HttpStatus> updatePerson(@RequestBody @Valid PersonDTO personDTO,
                               BindingResult bindingResult,
                               @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            throw new RuntimeException("Error update person");
        }
        peopleService.updatePerson(id, personDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/deletePerson")
    public ResponseEntity<HttpStatus> deletePerson(@PathVariable("id") Long id) {
        peopleService.deletePerson(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
