package org.rama.googleapi.exceptions;

public class NotAuthorizedException extends RuntimeException {
    public NotAuthorizedException(String message) {
        super(message);
        System.out.println(message);
    }
}
