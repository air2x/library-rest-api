package ru.maxima.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.maxima.dto.PersonLoginDTO;
import ru.maxima.dto.PersonRegDTO;
import ru.maxima.model.Person;
import ru.maxima.services.RegistrationService;
import ru.maxima.util.exeptions.PersonNotCreateException;
import ru.maxima.util.jwt.JWTUtil;
import ru.maxima.util.exeptions.PersonErrorResponse;
import ru.maxima.util.validation.PersonValidator;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final PersonValidator personValidator;
    private final RegistrationService registrationService;
    private final JWTUtil jwtUtil;
    private final ModelMapper mapper;
    private final AuthenticationManager authenticationManager;


    @Autowired
    public AuthController(PersonValidator personValidator, RegistrationService registrationService, JWTUtil jwtUtil,
                          ModelMapper mapper, AuthenticationManager authenticationManager) {
        this.personValidator = personValidator;
        this.registrationService = registrationService;
        this.jwtUtil = jwtUtil;
        this.mapper = mapper;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/registration")
    public Map<String, String> performRegistration(@RequestBody @Valid PersonRegDTO personRegDTO,
                                                   BindingResult bindingResult) {
        Person person = mapper.map(personRegDTO, Person.class);
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new PersonNotCreateException();
        }
        String token = jwtUtil.generateToken(person.getEmail());
        registrationService.register(person);
        return Map.of("jwt-token", token);
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody PersonLoginDTO personLoginDTO) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(personLoginDTO.getEmail(), personLoginDTO.getPassword());

        try {
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (AuthenticationException e) {
            return Map.of("message", "500");
        }

        String token = jwtUtil.generateToken(personLoginDTO.getEmail());
        return Map.of("jwt - token", token);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotCreateException ex) {
        PersonErrorResponse response = new PersonErrorResponse("Person is not created", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}
