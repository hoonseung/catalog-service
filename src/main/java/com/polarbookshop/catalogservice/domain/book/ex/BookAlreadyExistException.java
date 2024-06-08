package com.polarbookshop.catalogservice.domain.book.ex;

import com.polarbookshop.catalogservice.exception.ClientException;

public class BookAlreadyExistException extends ClientException {

    public BookAlreadyExistException(String isnbn) {
        super("해당 " + isnbn +"번호를 가지는 도서가 이미 존재합니다.");
    }
}
