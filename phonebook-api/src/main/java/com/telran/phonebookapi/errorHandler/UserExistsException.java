package com.telran.phonebookapi.errorHandler;



public class UserExistsException extends RuntimeException {
    public UserExistsException(String message) {
        super(message);
    }
}
