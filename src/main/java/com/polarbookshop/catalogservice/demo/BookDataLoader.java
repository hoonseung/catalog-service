package com.polarbookshop.catalogservice.demo;

import com.polarbookshop.catalogservice.domain.book.Book;
import com.polarbookshop.catalogservice.domain.book.BookRepository;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Component
@Profile("testdata")
public class BookDataLoader {


    private final BookRepository bookRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void loadBookTestData(){
        bookRepository.deleteAll();
        var faker = new Faker();
        var rd = new Random();
        var books = new ArrayList<Book>();

        IntStream.range(0, 5).forEach(i -> {
            var book = Book.of("1234" + rd.nextInt(10) + "678"+ rd.nextInt( 10) + rd.nextInt(10),
                    faker.book().title(),
                    faker.book().author(),
                    9.90,
                    "polar");
            books.add(book);
        });

        bookRepository.saveAll(books);
    }

}
