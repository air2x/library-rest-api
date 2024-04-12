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

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final PeopleRepository peopleRepository;
    private final BooksRepository booksRepository;

    private final PeopleService peopleService;
    private final ModelMapper mapper;

    @Autowired
    public BooksService(PeopleRepository peopleRepository, BooksRepository booksRepository, PeopleService peopleService, ModelMapper mapper) {
        this.peopleRepository = peopleRepository;
        this.booksRepository = booksRepository;
        this.peopleService = peopleService;
        this.mapper = mapper;
    }

    public List<BookDTO> findAllBooks() {
        List<Book> books = booksRepository.findAll();
        List<BookDTO> booksDTO = new ArrayList<>();
        for (Book book : books) {
            booksDTO.add(mapper.map(book, BookDTO.class));
        };
        return booksDTO;
    }

    @PreAuthorize("hasRole(T(ru.maxima.model.enums.Role).ROLE_ADMIN)")
    public BookDTO findOneBook(Long id) {
        Optional<Book> foundBook = booksRepository.findById(id);
        return mapper.map(foundBook, BookDTO.class);
    }

    @PreAuthorize("hasRole(T(ru.maxima.model.enums.Role).ROLE_ADMIN)")
    @Transactional
    public void saveBook(BookDTO bookDTO) {
        Book book = mapper.map(bookDTO, Book.class);
        book.setCreatedAt(LocalDateTime.now());
//        book.setCreatedPerson();
        booksRepository.save(book);
    }

    @PreAuthorize("hasRole(T(ru.maxima.model.enums.Role).ROLE_ADMIN)")
    @Transactional
    public void updateBook(Long id, BookDTO updateBook) {
        Book book = findOneBook(id);
        updateBook.setId(id);
        updateBook.setNameOfBook(updateBook.getNameOfBook());
        updateBook.setAuthorOfBook(updateBook.getAuthorOfBook());
        updateBook.setYearOfWritingBook(updateBook.getYearOfWritingBook());
        booksRepository.save(updateBook);
    }

    @PreAuthorize("hasRole(T(ru.maxima.model.enums.Role).ROLE_ADMIN)")
    @Transactional
    public void deleteBook(Long id) {
        Optional<Book> book = booksRepository.findById(id);

    }

    @PreAuthorize("hasRole(T(ru.maxima.model.enums.Role).ROLE_ADMIN)")
    @Transactional
    public void assignABook(Long bookId, PersonDTO personDTO) {
        Optional<Person> person = peopleRepository.findByEmail(personDTO.getEmail());
        Book book = booksRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + bookId));
        if (person.isEmpty()) {
            peopleService.savePerson(personDTO);
        }
        book.setOwner(person.orElseThrow());
        booksRepository.save(book);
    }

    @PreAuthorize("hasRole(T(ru.maxima.model.enums.Role).ROLE_ADMIN)")
    @Transactional
    public void freeTheBook(Long bookId) {
        Book book = booksRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + bookId));
        book.setOwner(null);
        booksRepository.save(book);
    }
}
