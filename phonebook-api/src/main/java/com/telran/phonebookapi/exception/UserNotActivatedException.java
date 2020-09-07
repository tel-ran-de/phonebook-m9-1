package com.telran.phonebookapi.exception;

public class UserNotActivatedException extends RuntimeException {
    public UserNotActivatedException(String message) {
        super(message);
    }
}
