package org.rama.googleapi.exceptions;

import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoDataFoundException.class)
    public String handleNoDataFoundException(NoDataFoundException ex) {
        return "No data found. Please try again." + ex.getMessage();
    }
    @ExceptionHandler(BadRequestException.class)
    public String handleBadRequestException(BadRequestException ex) {
        return "Bad Request" + ex.getMessage();
    }

    @ExceptionHandler(NoSpreadSheetFoundException.class)
    public String handleNoSpreadSheetFoundException(NoSpreadSheetFoundException ex) {
        return "No Spreadsheet found" + ex.getMessage();
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public String handleNotAuthorizedException(NotAuthorizedException ex) {
        return "Forbidden" + ex.getMessage();
    }

}
