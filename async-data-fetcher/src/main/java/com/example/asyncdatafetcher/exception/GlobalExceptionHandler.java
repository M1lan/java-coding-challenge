package com.example.asyncdatafetcher.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

/**
 * Global exception handler to catch and handle exceptions across the application.
 *
 * <p>This class uses {@link RestControllerAdvice} to globally intercept exceptions thrown by REST
 * controllers. When an exception occurs, it returns a {@link Mono} wrapping a {@link
 * ResponseEntity} containing an error message.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles any {@link Exception} thrown in the application.
   *
   * <p>This method captures all exceptions and returns a {@link Mono} containing a {@link
   * ResponseEntity} with a status of {@link HttpStatus#INTERNAL_SERVER_ERROR} and a simple error
   * message.
   *
   * @param ex the exception that was thrown
   * @return a {@link Mono} that emits a {@link ResponseEntity} with an error message
   */
  @ExceptionHandler(Exception.class)
  public Mono<ResponseEntity<String>> handleException(Exception ex) {
    return Mono.just(
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Internal Server Error: " + ex.getMessage()));
  }
}
