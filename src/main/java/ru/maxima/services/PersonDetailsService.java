package ru.maxima.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.maxima.model.Person;
import ru.maxima.security.PersonDetails;
import ru.maxima.util.Exeptions.PersonNotFoundException;

@Service
public class PersonDetailsService implements UserDetailsService {

    private final PeopleService peopleService;

    @Autowired
    public PersonDetailsService(PeopleService peopleService) {
        this.peopleService = peopleService;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Person person = null;
        try {
            person = peopleService.findByEmail(email);
        } catch (PersonNotFoundException e) {
            throw new PersonNotFoundException();
        }
        return new PersonDetails(person);
    }
}
