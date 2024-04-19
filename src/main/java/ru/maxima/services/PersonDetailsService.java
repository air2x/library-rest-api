package ru.maxima.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.maxima.model.Person;
import ru.maxima.security.PersonDetails;

@Service
public class PersonDetailsService implements UserDetailsService {

    private final PeopleService peopleService;

    @Autowired
    public PersonDetailsService(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Person person = peopleService.findByEmail(name);
        if (person == null) {
            throw new UsernameNotFoundException("User not found from PersonDetailsService");
        }
        return new PersonDetails(person);
    }
}
