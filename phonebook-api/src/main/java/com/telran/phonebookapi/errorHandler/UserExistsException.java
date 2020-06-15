package com.telran.phonebookapi.errorHandler;

public class UserExistsException extends RuntimeException {

    public UserExistsException(String email){
        super(String.format("User with %s email already exists", email));
    }
}
