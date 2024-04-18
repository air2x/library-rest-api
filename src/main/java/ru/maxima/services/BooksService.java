package ru.maxima.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maxima.dto.BookDTO;
import ru.maxima.dto.PersonDTO;
import ru.maxima.model.*;
import ru.maxima.repositories.BooksRepository;
import ru.maxima.repositories.PeopleRepository;
import ru.maxima.security.PersonDetails;
import ru.maxima.util.Exeptions.PersonNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final PeopleRepository peopleRepository;
    private final BooksRepository booksRepository;
    private final ModelMapper mapper;

    @Autowired
    public BooksService(PeopleRepository peopleRepository, BooksRepository booksRepository, ModelMapper mapper) {
        this.peopleRepository = peopleRepository;
        this.booksRepository = booksRepository;
        this.mapper = mapper;
    }

    public List<BookDTO> getAllBooks() {
        List<Book> books = booksRepository.findAll();
        List<BookDTO> booksDTO = new ArrayList<>();
        for (Book book : books) {
            if (book.getRemovedAt() == null) {
                booksDTO.add(mapper.map(book, BookDTO.class));
            }
        }
        return booksDTO;
    }

    @PreAuthorize("hasAuthority(T(ru.maxima.model.enums.Role).ADMIN.getName())")
    public Book getOneBook(Long id) {
        return booksRepository.findById(id).orElseThrow(null);
    }

    @PreAuthorize("hasAuthority(T(ru.maxima.model.enums.Role).ADMIN.getName())")
    @Transactional
    public void saveBook(BookDTO bookDTO, PersonDetails personDetails) {
        Book book = mapper.map(bookDTO, Book.class);
        book.setCreatedAt(LocalDateTime.now());
        book.setCreatedPerson(findPersonByEmail(personDetails));
        booksRepository.save(book);
    }

    @PreAuthorize("hasAuthority(T(ru.maxima.model.enums.Role).ADMIN.getName())")
    @Transactional
    public void updateBook(Long id, BookDTO updateBook, PersonDetails personDetails) {
        Book book = getOneBook(id);
        book.setName(updateBook.getName());
        book.setAnnotation(updateBook.getAnnotation());
        book.setYearOfProduction(updateBook.getYearOfProduction());
        book.setUpdatedAt(LocalDateTime.now());
        book.setUpdatedPerson(findPersonByEmail(personDetails));
//        book.setOwner(updateBook.getOwner());
        booksRepository.save(book);
    }

    @PreAuthorize("hasAuthority(T(ru.maxima.model.enums.Role).ADMIN.getName())")
    @Transactional
    public void deleteBook(Long id, PersonDetails personDetails) {
        Book book = getOneBook(id);
        book.setRemovedAt(LocalDateTime.now());
        book.setRemovedPerson(findPersonByEmail(personDetails));
    }

    @PreAuthorize("hasAuthority(T(ru.maxima.model.enums.Role).ADMIN.getName())")
    @Transactional
    public void assignABook(Long bookId, PersonDTO personDTO) {
        Optional<Person> person = peopleRepository.findByName(personDTO.getEmail());
        Book book = booksRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + bookId));
//        if (person.isEmpty()) {
//            peopleService.savePerson(personDTO);
//        }
        book.setOwner(person.orElseThrow());
        booksRepository.save(book);
    }

    @PreAuthorize("hasAuthority(T(ru.maxima.model.enums.Role).ADMIN.getName())")
    @Transactional
    public void freeTheBook(Long bookId) {
        Book book = booksRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + bookId));
        book.setOwner(null);
        booksRepository.save(book);
    }

    public List<BookDTO> showFreeBooks() {
        List<Book> books = booksRepository.findAll();
        List<BookDTO> booksDTO = new ArrayList<>();
        for (Book book : books) {
            if (book.getOwner() == null) {
                booksDTO.add(mapper.map(book, BookDTO.class));
            }
        }
        return booksDTO;
    }

    public Book findOneBookByName(String name) {
        return booksRepository.findByName(name).orElseThrow(null);
    }


    private Person findPersonByEmail(PersonDetails personDetails) {
        return peopleRepository.findByEmail(personDetails.getUsername()).orElseThrow(PersonNotFoundException::new);
    }
}
