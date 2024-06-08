package com.polarbookshop.catalogservice.domain.book.ex;

import com.polarbookshop.catalogservice.exception.ClientException;

public class BookNotFoundException extends ClientException {



    public BookNotFoundException(String isbn) {
        super("해당 " + isbn + "번호를 가지는 도서를 찾을 수 없습니다.");
    }
}
