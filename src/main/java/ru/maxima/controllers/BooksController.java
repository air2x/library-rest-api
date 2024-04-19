package ru.maxima.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.maxima.dto.*;
import ru.maxima.model.Book;
import ru.maxima.model.Person;
import ru.maxima.model.enums.Role;
import ru.maxima.security.PersonDetails;
import ru.maxima.services.BooksService;
import ru.maxima.services.PeopleService;
import ru.maxima.util.exeptions.BookBusyException;
import ru.maxima.util.exeptions.BookErrorResponse;
import ru.maxima.util.exeptions.BookNotFoundException;


import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BooksController {

    private final BooksService booksService;
    private final PeopleService peopleService;
    private final ModelMapper mapper;

    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService, ModelMapper mapper) {
        this.booksService = booksService;
        this.peopleService = peopleService;
        this.mapper = mapper;
    }

    @GetMapping("/showAllBooks")
    public ResponseEntity<List<BookDTO>> showAllBooks() {
        return ResponseEntity.ok(booksService.getAllBooks());
    }

    @GetMapping("/showAllFreeBooks")
    public ResponseEntity<List<BookDTO>> showFreeBooks() {
        return ResponseEntity.ok(booksService.getFreeBooks());
    }

    @GetMapping("/showCoverBooks")
    public ResponseEntity<List<BookCoverDTO>> showCoverBooks() {
        return ResponseEntity.ok(booksService.getCoverBooks());
    }

    @GetMapping("/{id}/showBook")
    public BookResponseDTO showBook(@PathVariable("id") Long id,
                                    @AuthenticationPrincipal PersonDetails personDetails) {
        Person person = peopleService.findByEmail(personDetails.getUsername());
        if (person.getRole().equals(Role.USER.getName())) {
            throw new BookBusyException();
        }
        Book book = booksService.getOneBook(id);
        BookResponseDTO bookResponseDTO = mapper.map(book, BookResponseDTO.class);
        bookResponseDTO.setOwner(mapper.map(book.getOwner(), PersonDTO.class));
        return bookResponseDTO;
    }

    @GetMapping("/{id}/getOwner")
    public PersonDTO getBookOwner(@PathVariable("id") Long id) {
        Book book = booksService.getOneBook(id);
        return mapper.map(book.getOwner(), PersonDTO.class);
    }

    @PostMapping("/{id}/assign")
    public ResponseEntity<HttpStatus> assignABook(@PathVariable("id") Long id,
                                                  @RequestBody PersonEmailDTO personEmailDTO) {
        booksService.assignABook(id, personEmailDTO.getEmail());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/{id}/freeTheBook")
    public ResponseEntity<HttpStatus> freeTheBook(@PathVariable("id") Long id) {
        booksService.freeTheBook(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/{id}/takeBook")
    public ResponseEntity<HttpStatus> takeBook(@PathVariable("id") Long id,
                                               @AuthenticationPrincipal PersonDetails personDetails) {
        booksService.takeBook(id, personDetails);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/createBook")
    public ResponseEntity<HttpStatus> createBook(@RequestBody @Valid BookDTO bookDTO,
                                                 BindingResult bindingResult,
                                                 @AuthenticationPrincipal PersonDetails personDetails) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        booksService.saveBook(bookDTO, personDetails);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/{id}/updateBook")
    public ResponseEntity<HttpStatus> updateBook(@RequestBody @Valid BookDTO bookDTO,
                                                 BindingResult bindingResult,
                                                 @PathVariable("id") Long id,
                                                 @AuthenticationPrincipal PersonDetails personDetails) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        booksService.updateBook(id, bookDTO, personDetails);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/deleteBook")
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable("id") Long id,
                                                 @AuthenticationPrincipal PersonDetails personDetails) {
        booksService.deleteBook(id, personDetails);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<BookErrorResponse> handleException(BookNotFoundException ex) {
        BookErrorResponse response = new BookErrorResponse("Book wasn't found", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<BookErrorResponse> handleException(BookBusyException ex) {
        BookErrorResponse response = new BookErrorResponse("This book busy", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
