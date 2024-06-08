package com.polarbookshop.catalogservice.domain.book;


import com.polarbookshop.catalogservice.domain.book.ex.BookAlreadyExistException;
import com.polarbookshop.catalogservice.domain.book.ex.BookNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }



    public Iterable<Book> viewBookList(){
        return bookRepository.findAll();
    }

    public Book viewBookDetails(String isbn){
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException(isbn));
    }


    public Book addBookToCatalog(Book book){
        if(bookRepository.existByIsbn(book.isbn())){
            throw new BookAlreadyExistException(book.isbn());
        }
        return bookRepository.save(book);
    }

    public void removeBookFromCatalog(String isbn){
        bookRepository.deleteByIsbn(isbn);
    }

    public Book editBookDetails(String isbn, Book book){
        return bookRepository.findByIsbn(isbn)
                .map(existingBook -> {
                    var updateBook = new Book(
                            existingBook.isbn(),
                            book.title(),
                            book.author(),
                            book.price()
                    );
                    return bookRepository.save(updateBook);
                }).orElseThrow(() -> new BookNotFoundException(isbn));
    }


}