package com.polarbookshop.catalogservice;

import com.polarbookshop.catalogservice.domain.book.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("intergration")
class CatalogServiceApplicationTests {

    @Autowired
    private  WebTestClient webTestClient;

    @Test
    void whenPostRequestThenBookCreated() {
        var expectBook = Book.of("1234567890", "title", "devjohn", 9.9, "polar");

        webTestClient
                .post()
                .uri("/books")
                .bodyValue(expectBook)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Book.class).value(actualBook  -> {
                    assertThat(actualBook).isNotNull();
                    assertThat(actualBook.isbn()).isEqualTo(expectBook.isbn());
                });
    }

}
