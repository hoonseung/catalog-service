package com.polarbookshop.catalogservice.web;


import com.polarbookshop.catalogservice.domain.book.Book;
import com.polarbookshop.catalogservice.domain.book.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/books")
@RestController
public class BookController {

    private final BookService bookService;



    @GetMapping
    public Iterable<Book> get(){
        log.info("Fetching the list of books in the catalog");
        return bookService.viewBookList();
    }

    @GetMapping("/{isbn}")
    public Book getByIsbn(@PathVariable("isbn") String isbn){
        return bookService.viewBookDetails(isbn);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Book addBook(@RequestBody @Valid Book book){
        return bookService.addBookToCatalog(book);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{isbn}")
    public void deleteByIsbn(@PathVariable("isbn") String isbn){
        bookService.removeBookFromCatalog(isbn);
    }

    @PutMapping("/{isbn}")
    public Book putByIsbn(@PathVariable("isbn") String isbn,
                          @RequestBody @Valid Book book){
        return bookService.editBookDetails(isbn, book);
    }

}
