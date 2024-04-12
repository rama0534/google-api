package org.rama.googleapi.exceptions;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoDataFoundException.class)
    public String handleNoDataFoundException(NoDataFoundException ex) {
        return "No data found. Please try again.";
    }
}
