package com.polarbookshop.catalogservice.domain.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record Book(
        @NotBlank(message = "도서번호는(ISBN) 필수입니다.")
        @Pattern(regexp = "^([0-9]{10}|[0-9]{13})$", message = "올바르지 않은 도서번호 양식입니다.")
        String isbn,

        @NotBlank(message = "도서제목은 필수입니다.")
        String title,

        @NotBlank(message = "저자는 필수입니다.")
        String author,

        @NotNull(message = "도서가격은 필수입니다.")
        @Positive(message = "도서가격은 0보다 커야합니다.") // null 과 0보다 작은 값을 허용 x
        Double price
) {
}
