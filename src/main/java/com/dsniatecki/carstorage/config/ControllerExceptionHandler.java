package com.dsniatecki.carstorage.config;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.NoSuchElementException;

@RestControllerAdvice
class ControllerExceptionHandler {

    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class, ConstraintViolationException.class})
    ResponseEntity<String> handleBadRequests(Exception exc) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(NoSuchElementException.class)
    ResponseEntity<Void> handleNoSuchElementException(NoSuchElementException exc) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException exc) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
