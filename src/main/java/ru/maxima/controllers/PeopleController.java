package ru.maxima.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.maxima.dto.BookDTO;
import ru.maxima.dto.PersonDTO;
import ru.maxima.dto.PersonRegDTO;
import ru.maxima.model.Person;
import ru.maxima.security.PersonDetails;
import ru.maxima.services.PasswordEncodeService;
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
    private final PasswordEncodeService passwordEncodeService;

    @Autowired
    public PeopleController(PeopleService peopleService, ModelMapper mapper, PasswordEncodeService passwordEncodeService) {
        this.peopleService = peopleService;
        this.mapper = mapper;
        this.passwordEncodeService = passwordEncodeService;
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

    @GetMapping("/showAboutMe")
    public PersonDTO getPerson(@AuthenticationPrincipal PersonDetails personDetails) {
        Person person = peopleService.getMyPerson(personDetails);
        return mapper.map(person, PersonDTO.class);
    }

    @GetMapping("/showMyBooks")
    public List<BookDTO> getMyBooks(@AuthenticationPrincipal PersonDetails personDetails) {
        return peopleService.getMyBooks(personDetails);
    }


    @PostMapping("/createPerson")
    public ResponseEntity<HttpStatus> createPerson(@RequestBody @Valid PersonRegDTO personRegDTO,
                                                   BindingResult bindingResult,
                                                   @AuthenticationPrincipal PersonDetails userDetails) {
        personRegDTO.setPassword(passwordEncodeService.encodePassword(personRegDTO.getPassword()));
        Person person = mapper.map(personRegDTO, Person.class);
        if (bindingResult.hasErrors()) {
            throw new RuntimeException("Error create person");
        }
        peopleService.savePerson(person, userDetails);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/deletePerson")
    public ResponseEntity<HttpStatus> deletePerson(@PathVariable("id") Long id,
                                                   @AuthenticationPrincipal PersonDetails userDetails) {
        peopleService.deletePerson(id, userDetails);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException ex) {
        PersonErrorResponse response = new PersonErrorResponse("Person wasn't found", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
