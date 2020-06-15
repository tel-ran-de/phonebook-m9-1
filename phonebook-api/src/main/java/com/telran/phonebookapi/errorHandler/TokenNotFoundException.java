package com.telran.phonebookapi.errorHandler;

public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException(){
        super("User is not registered");
    }
}
