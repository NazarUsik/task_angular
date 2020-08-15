package com.nixsolutions.usik.exception;

import org.springframework.http.HttpStatus;

public class UserServiceException extends Exception {

    private final HttpStatus status;

    public UserServiceException(String message, HttpStatus status, Throwable cause) {
        super(message + " HttpStatus: " + status.toString(), cause);
        this.status = status;
    }

    public UserServiceException(String message, HttpStatus status) {
        super(message + " HttpStatus: " + status.toString());
        this.status = status;
    }

    public UserServiceException(HttpStatus status, Throwable cause) {
        super("HttpStatus: " + status.toString(), cause);
        this.status = status;
    }

    public UserServiceException(HttpStatus status) {
        super("HttpStatus: " + status.toString());
        this.status = status;
    }

    public UserServiceException(String message) {
        super(message);
        this.status = null;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
