package com.spring.jwt.SpringAppWithJwt.exceptionObjects;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
