package com.polarbookshop.catalogservice.domain.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.springframework.data.annotation.*;

import java.time.Instant;

public record Book(

        @Id
        Long id,

        @NotBlank(message = "도서번호는(ISBN) 필수입니다.")
        @Pattern(regexp = "^([0-9]{10}|[0-9]{13})$", message = "올바르지 않은 도서번호 양식입니다.")
        String isbn,

        @NotBlank(message = "도서제목은 필수입니다.")
        String title,

        @NotBlank(message = "저자는 필수입니다.")
        String author,

        @NotNull(message = "도서가격은 필수입니다.")
        @Positive(message = "도서가격은 0보다 커야합니다.") // null 과 0보다 작은 값을 허용 x
        Double price,

        @NotBlank(message = "출판사는 필수입니다.")
        String publisher,

        @CreatedDate
        Instant createdDate,

        @CreatedBy
        String createdBy,

        @LastModifiedDate
        Instant lastModifiedDate,

        @LastModifiedBy
        String lastModifiedBy,

        @Version // 낙관적 잠금, 자동증가, 동시 업데이트 방지
        int version
) {

        public static Book of(String isbn, String title, String author, Double price, String publisher){
                return new Book(
                        null,
                        isbn,
                        title,
                        author,
                        price,
                        publisher,
                        null,
                        null,
                        null,
                        null,
                        0
                );
        }
}
