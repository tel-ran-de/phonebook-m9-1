package com.telran.phonebookapi.errorHandler;




public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException(String message) {
        super(message);
    }
}
