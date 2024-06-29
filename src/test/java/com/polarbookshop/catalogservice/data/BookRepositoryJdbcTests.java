package com.polarbookshop.catalogservice.data;


import com.polarbookshop.catalogservice.config.DataConfig;
import com.polarbookshop.catalogservice.domain.book.Book;
import com.polarbookshop.catalogservice.domain.book.BookRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Import(DataConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("intergration")
 class BookRepositoryJdbcTests {

    @Autowired
    private BookRepository bookRepository;


    @DisplayName("미인증 상태에서 데이터 등록 시 데이터 감사 테스트")
    @Test
    void whenCreateBookNotAuthenticatedThenAuditMetadata(){
        var bookToCreate = Book.of("1234567890", "author", "author", 1000.0, "publisher");

        Book psBook = bookRepository.save(bookToCreate);

        assertThat(psBook.createdBy()).isNull();
        assertThat(psBook.lastModifiedBy()).isNull();
    }

    @DisplayName("인증 상태에서 데이터 등록 시 데이터 감사 테스트")
    @WithMockUser(username = "devJohn")
    @Test
    void whenCreatedBookAuthenticatedThenAuditMetadata(){
        var bookToCreate = Book.of("1234567890", "author", "author", 1000.0, "publisher");

        Book psBook = bookRepository.save(bookToCreate);
        assertThat(psBook.createdBy()).isEqualTo("devJohn");
        assertThat(psBook.lastModifiedBy()).isEqualTo("devJohn");

    }

}
