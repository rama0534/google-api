package org.rama.googleapi.exceptions;

public class NoDataFoundException extends RuntimeException {
    public NoDataFoundException(String message) {
        super(message);
        System.out.println(message);
    }

    public NoDataFoundException(String message, Throwable cause) {
        super(message, cause);
        System.out.println(message);
    }
}
