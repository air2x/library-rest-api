package ru.maxima.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.maxima.dto.BookCoverDTO;
import ru.maxima.dto.BookDTO;
import ru.maxima.dto.PersonDTO;
import ru.maxima.model.Book;
import ru.maxima.security.PersonDetails;
import ru.maxima.services.BooksService;
import ru.maxima.util.BookErrorResponse;
import ru.maxima.util.Exeptions.BookNotFoundException;


import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BooksController {

    private final BooksService booksService;
    private final ModelMapper mapper;

    @Autowired
    public BooksController(BooksService booksService, ModelMapper mapper) {
        this.booksService = booksService;
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
    public BookDTO showBook(@PathVariable("id") Long id) {
        Book book = booksService.getOneBook(id);
        return mapper.map(book, BookDTO.class);
    }

    @GetMapping("/{id}/getOwner")
    public PersonDTO getBookOwner(@PathVariable("id") Long id) {
        Book book = booksService.getOneBook(id);
        return mapper.map(book.getOwner(), PersonDTO.class);
    }

    @PostMapping("/{id}/assign")
    public ResponseEntity<HttpStatus> assignABook(@PathVariable("id") Long id,
                                                  @RequestBody PersonDTO person) {
        booksService.assignABook(id, person);
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
}
