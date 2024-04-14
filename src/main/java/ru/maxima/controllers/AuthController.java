package ru.maxima.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.maxima.dto.PersonRegDTO;
import ru.maxima.model.Person;
import ru.maxima.services.RegistrationService;
import ru.maxima.validation.PersonValidator;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final PersonValidator personValidator;
    private final RegistrationService registrationService;

    @Autowired
    public AuthController(PersonValidator personValidator, RegistrationService registrationService) {
        this.personValidator = personValidator;
        this.registrationService = registrationService;
    }

//    @GetMapping("/login")
//    public String loginPage() {
//        return "auth/login";
//    }

//    @GetMapping("/registration")
//    public String registrationPage(@ModelAttribute("person") Person person) {
//        return "auth/registration";
//    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> performRegistration(@RequestBody @Valid PersonRegDTO personRegDTO,
                                                          BindingResult bindingResult) {
        personValidator.validate(personRegDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new RuntimeException("Error registration");
        }
        registrationService.register(personRegDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
