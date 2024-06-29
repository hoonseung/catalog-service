package com.polarbookshop.catalogservice.web;


import com.polarbookshop.catalogservice.config.SecurityConfig;
import com.polarbookshop.catalogservice.domain.book.BookService;
import com.polarbookshop.catalogservice.domain.book.ex.BookNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@WebMvcTest(BookController.class)
@Import(SecurityConfig.class)
 class BookControllerMvcTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private JwtDecoder jwtDecoder; // 키클록 엔드포인트를 통해서 공개키를 가져와 jwtDecoder 객체로 jwt 파싱을 모의 객체 주입으로 상호작용 하지 않는다.


    @Test
    void whenGetBookNotExistingThenShouldReturn404() throws Exception {
        String isbn = "12345678901";
        given(bookService.viewBookDetails(isbn))
                .willThrow(BookNotFoundException.class);

        mvc.perform(get("/books/", isbn))
                .andExpect(status().isNotFound());
    }


    @Test
    void whenDeleteBookWithEmployeeRoleThenShouldReturn204() throws Exception{
        var isbn = "12345678901";

        mvc.perform(delete("/books/{isbn}", isbn)
                .with(SecurityMockMvcRequestPostProcessors.jwt()
                        .authorities(new SimpleGrantedAuthority("ROLE_employee")))
        ).andExpect(status().isNoContent());
    }

    @Test
    void whenDeleteBookWithCustomerRoleThenShouldReturn403() throws Exception{
        var isbn = "12345678901";

        mvc.perform(delete("/books/{isbn}", isbn)
                .with(SecurityMockMvcRequestPostProcessors.jwt()
                        .authorities(new SimpleGrantedAuthority("ROLE_customer"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenDeleteBookNotAuthenticatedThenShouldReturn401() throws Exception{
        var isbn = "12345678901";

        mvc.perform(delete("/books/{isbn}", isbn))
                .andExpect(status().isUnauthorized());
    }




}
