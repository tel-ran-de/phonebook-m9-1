package com.telran.phonebookapi.errorHandler;

public class UserDoesntExistException extends RuntimeException {

    public UserDoesntExistException(String message) {
        super(message);
    }
}
