package com.vsk.courtrf.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler
    public static ResponseEntity handleAllExceptions(Exception ex, WebRequest request) {
        ErrorDetails err = new ErrorDetails(
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>( err, HttpStatus.BAD_REQUEST);
        //return new ResponseEntity<>( ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
