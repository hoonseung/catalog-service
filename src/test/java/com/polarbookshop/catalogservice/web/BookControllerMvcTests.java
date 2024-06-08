package com.polarbookshop.catalogservice.web;


import com.polarbookshop.catalogservice.domain.book.BookService;
import com.polarbookshop.catalogservice.domain.book.ex.BookNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(BookController.class)
 class BookControllerMvcTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;


    @DisplayName("ISBN 찾을 수 없음")
    @Test
    void whenGetBookNotExistingThenShouldReturn404() throws Exception {
        String isbn = "12345678901";


        given(bookService.viewBookDetails(isbn))
                .willThrow(BookNotFoundException.class);

        mvc.perform(get("/books/", isbn))
                .andExpect(status().isNotFound());
    }
}
