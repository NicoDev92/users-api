package com.nicode.users.usersapi.exceptions;

public class DataAccessException extends RuntimeException{

    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
