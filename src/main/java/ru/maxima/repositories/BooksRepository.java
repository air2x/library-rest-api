package ru.maxima.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.maxima.model.Book;


@Repository
public interface BooksRepository extends JpaRepository<Book, Long> {

}
