package com.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<Object> handleUsernameAlreadyTakenResponse(BadRequestException e){
        ApiException ap = new ApiException(
                e.getMessage()
        );
        return new ResponseEntity<>(ap, HttpStatus.BAD_REQUEST);
    }

}