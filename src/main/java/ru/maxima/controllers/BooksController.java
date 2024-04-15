package ru.maxima.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.maxima.dto.BookDTO;
import ru.maxima.dto.PersonDTO;
import ru.maxima.model.Book;
import ru.maxima.repositories.BooksRepository;
import ru.maxima.services.BooksService;
import ru.maxima.services.PeopleService;


import java.util.List;

@RestController
@RequestMapping("/books")
public class BooksController {

    private final BooksService booksService;
    private final PeopleService peopleService;
    private final ModelMapper mapper;
    private final BooksRepository booksRepository;

    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService, ModelMapper mapper,
                           BooksRepository booksRepository) {
        this.booksService = booksService;
        this.peopleService = peopleService;
        this.mapper = mapper;
        this.booksRepository = booksRepository;
    }

    @GetMapping("/showAllBooks")
    public ResponseEntity<List<BookDTO>> showAllBooks() {
        return ResponseEntity.ok(booksService.findAllBooks());
    }

    @GetMapping("/showAllFreeBooks")
    public ResponseEntity<List<BookDTO>> showFreeBooks() {
        return ResponseEntity.ok(booksService.showFreeBooks());
    }

    @GetMapping("/{id}/showBook")
    public BookDTO showBook(@PathVariable("id") Long id) {
        Book book = booksService.findOneBook(id);
        return mapper.map(book, BookDTO.class);
    }

    @GetMapping("/{id}/getOwner")
    public PersonDTO getBookOwner(@PathVariable("id") Long id) {
        Book book = booksService.findOneBook(id);
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

    @PostMapping("/createBook")
    public ResponseEntity<HttpStatus> createBook(@RequestBody @Valid BookDTO bookDTO,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        booksService.saveBook(bookDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/{id}/updateBook")
    public ResponseEntity<HttpStatus> updateBook(@RequestBody @Valid BookDTO bookDTO,
                                                 BindingResult bindingResult,
                                                 @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        booksService.updateBook(id, bookDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/deleteBook")
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable("id") Long id) {
        booksService.deleteBook(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
