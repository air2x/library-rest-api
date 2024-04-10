package ru.maxima.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.maxima.model.Book;
import ru.maxima.model.Person;
import ru.maxima.services.BooksService;
import ru.maxima.services.PeopleService;

@RestController
@RequestMapping("/books")
public class BooksController {

    private final BooksService booksService;
    private final PeopleService peopleService;

    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping
    public String showAllBooks(Model model) {
        model.addAttribute("books", booksService.findAllBooks());
        return "view-with-all-books";
    }

    @PostMapping("/{id}/assign")
    public String assignABook(@PathVariable("id") Long id,
                              @ModelAttribute("person") Person person) {
        booksService.assignABook(id, person);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String showBook(@PathVariable("id") Long id, Model model,
                           @ModelAttribute("person") Person person) {
        model.addAttribute("book", booksService.findOneBook(id));
        model.addAttribute("people", peopleService.findAllPeople());
        return "view-with-book-by-id";
    }

    @PostMapping("/{id}/freeTheBook")
    public String freeTheBook(@PathVariable("id") Long id) {
        booksService.freeTheBook(id);
        return "redirect:/books";
    }

    @GetMapping("/new")
    public String addNewBook(Model model) {
        model.addAttribute("book", new Book());
        return "view-to-create-new-book";
    }

    @PostMapping()
    public String createBook(@ModelAttribute("book") @Valid Book book,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "view-to-create-new-book";
        }
        booksService.saveBook(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editBook(Model model, @PathVariable("id") Long id) {
        model.addAttribute("book", booksService.findOneBook(id));
        return "view-to-edit-book";
    }

    @PostMapping("/{id}")
    public String updateBook(@ModelAttribute("book") @Valid Book book,
                             BindingResult bindingResult,
                             @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "view-to-edit-book";
        }
        booksService.updateBook(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        booksService.deleteBook(id);
        return "redirect:/books";
    }
}
