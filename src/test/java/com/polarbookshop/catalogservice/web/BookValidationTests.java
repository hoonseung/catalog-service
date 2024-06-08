package com.polarbookshop.catalogservice.web;

import com.polarbookshop.catalogservice.domain.book.Book;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class BookValidationTests {

    private static Validator validator;


    @BeforeAll
    static void setUp(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @DisplayName("성공 케이스")
    @Test
    void whenAllFieldCorrectThenValidationSucceeds(){
       var book =  new Book("1234567890","title","devjohn", 9.9);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        Assertions.assertThat(violations.isEmpty());
    }

    @DisplayName("실패 케이스")
    @Test
    void whenIsbnDefineButIncorrectThenValidationFails(){
        var book =  new Book("1a234567890","title","devjohn", 9.9);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        Assertions.assertThat(violations).hasSize(1);
        Assertions.assertThat(violations.iterator().next().getMessage().equals("올바르지 않은 도서번호 양식입니다."));
    }

}
