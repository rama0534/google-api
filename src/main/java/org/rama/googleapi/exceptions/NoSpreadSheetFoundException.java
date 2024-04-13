package org.rama.googleapi.exceptions;

public class NoSpreadSheetFoundException extends RuntimeException{
    public NoSpreadSheetFoundException(String message) {
        super(message);
        System.out.println(message);
    }
}
