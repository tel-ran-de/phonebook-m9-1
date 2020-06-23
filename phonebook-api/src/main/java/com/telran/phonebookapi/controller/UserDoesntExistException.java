package com.telran.phonebookapi.controller;

public class UserDoesntExistException extends RuntimeException {

    public UserDoesntExistException(String message) {
        super(message);
    }
}
