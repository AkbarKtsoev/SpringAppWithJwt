package com.spring.jwt.SpringAppWithJwt.exceptionObjects;

public class EventNotFoundException extends RuntimeException{
    public EventNotFoundException(String message) {
        super(message);
    }
}
