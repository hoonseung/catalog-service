package com.polarbookshop.catalogservice.domain.book;


import com.polarbookshop.catalogservice.domain.book.ex.BookAlreadyExistException;
import com.polarbookshop.catalogservice.domain.book.ex.BookNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BookService {

    private final BookRepository bookRepository;



    public Iterable<Book> viewBookList(){
        return bookRepository.findAll();
    }

    public Book viewBookDetails(String isbn){
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException(isbn));
    }


    @Transactional
    public Book addBookToCatalog(Book book){
        if(bookRepository.existsByIsbn(book.isbn())){
            throw new BookAlreadyExistException(book.isbn());
        }
        return bookRepository.save(book);
    }

    @Transactional
    public void removeBookFromCatalog(String isbn){
        bookRepository.deleteByIsbn(isbn);
    }


    @Transactional
    public Book editBookDetails(String isbn, Book book){
        return bookRepository.findByIsbn(isbn)
                .map(existingBook -> {
                    var updateBook = new Book(
                            existingBook.id(),
                            existingBook.isbn(),
                            book.title(),
                            book.author(),
                            book.price(),
                            existingBook.createDate(),
                            existingBook.lastModifiedDate(),
                            existingBook.version()
                    );
                    return bookRepository.save(updateBook);
                }).orElseThrow(() -> new BookNotFoundException(isbn));
    }


}
