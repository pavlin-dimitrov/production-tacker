package com.example.productiontracker.exception;

import com.example.productiontracker.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(value = {IllegalArgumentException.class})
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
    ErrorResponse errorResponse =
        new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(), ex.getMessage(), System.currentTimeMillis());

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }
}
