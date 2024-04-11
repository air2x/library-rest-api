package ru.maxima.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.maxima.dto.PersonDTO;
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


    @GetMapping("/new")
    public ResponseEntity<HttpStatus> addNewPerson(@RequestBody PersonDTO personDTO) {
        peopleService.savePerson(personDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> createPerson(@RequestBody @Valid PersonDTO personDTO,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RuntimeException("Error create person");
        }
        peopleService.savePerson(personDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @GetMapping("/{id}/edit")
//    public ResponseEntity<HttpStatus> editPerson(@PathVariable("id") Long id,
//                             @RequestBody PersonDTO personDTO) {
//        peopleService.updatePerson(id, personDTO);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    @PostMapping("/{id}")
    public ResponseEntity<HttpStatus> updatePerson(@RequestBody @Valid PersonDTO personDTO,
                               BindingResult bindingResult,
                               @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            throw new RuntimeException("Error update person");
        }
        peopleService.updatePerson(id, personDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePerson(@PathVariable("id") Long id) {
        peopleService.deletePerson(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
