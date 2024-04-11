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
    public void saveBook(Book book) {
        booksRepository.save(book);
    }

    @PreAuthorize("hasRole(T(ru.maxima.model.enums.Role).ROLE_ADMIN)")
    @Transactional
    public void updateBook(Long id, Book updateBook) {
        updateBook.setId(id);
        updateBook.setNameOfBook(updateBook.getNameOfBook());
        updateBook.setAuthorOfBook(updateBook.getAuthorOfBook());
        updateBook.setYearOfWritingBook(updateBook.getYearOfWritingBook());
        booksRepository.save(updateBook);
    }

    @PreAuthorize("hasRole(T(ru.maxima.model.enums.Role).ROLE_ADMIN)")
    @Transactional
    public void deleteBook(Long id) {
        booksRepository.deleteById(id);
    }

    @PreAuthorize("hasRole(T(ru.maxima.model.enums.Role).ROLE_ADMIN)")
    @Transactional
    public void assignABook(Long bookId, PersonDTO person) {
        Book book = booksRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + bookId));
        if (peopleRepository.findById(person.getId()).isEmpty()) {
            peopleRepository.save(person);
        }
        book.setOwner(person);
        booksRepository.save(book);
    }

    @PreAuthorize("hasRole(T(ru.maxima.libraryspringsecurity.model.enums.Role).ROLE_ADMIN)")
    @Transactional
    public void freeTheBook(Long bookId) {
        Book book = booksRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + bookId));
        book.setOwner(null);
        booksRepository.save(book);
    }
}
