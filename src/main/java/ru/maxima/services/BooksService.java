package ru.maxima.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maxima.model.*;
import ru.maxima.repositories.BooksRepository;
import ru.maxima.repositories.PeopleRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final PeopleRepository peopleRepository;
    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(PeopleRepository peopleRepository, BooksRepository booksRepository) {
        this.peopleRepository = peopleRepository;
        this.booksRepository = booksRepository;
    }

    public List<Book> findAllBooks() {
        return booksRepository.findAll();
    }

    @PreAuthorize("hasRole(T(ru.maxima.model.enums.Role).ROLE_ADMIN)")
    public Book findOneBook(Long id) {
        Optional<Book> foundBook = booksRepository.findById(id);
        return foundBook.orElse(null);
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
    public void assignABook(Long bookId, Person person) {
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
