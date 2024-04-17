package ru.maxima.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.maxima.dto.BookDTO;
import ru.maxima.dto.PersonDTO;
import ru.maxima.dto.PersonRegDTO;
import ru.maxima.model.Person;
import ru.maxima.services.PeopleService;
import ru.maxima.util.Exeptions.PersonNotFoundException;
import ru.maxima.util.PersonErrorResponse;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;
    private final ModelMapper mapper;

    @Autowired
    public PeopleController(PeopleService peopleService, ModelMapper mapper) {
        this.peopleService = peopleService;
        this.mapper = mapper;
    }

    @GetMapping("/showAllPeople")
    public ResponseEntity<List<PersonDTO>> showAllPeople() {
        return ResponseEntity.ok(peopleService.findAllPeople());
    }

    @GetMapping("/{id}/showPerson")
    public PersonDTO getPerson(@PathVariable("id") Long id) {
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
        Person person = mapper.map(personRegDTO, Person.class);
        if (bindingResult.hasErrors()) {
            throw new RuntimeException("Error create person");
        }
        peopleService.savePerson(person);
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

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException ex) {
        PersonErrorResponse response = new PersonErrorResponse("Person wasn't found", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
