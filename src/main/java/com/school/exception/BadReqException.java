package com.school.exception;

public class BadReqException extends RuntimeException{

    public BadReqException(String message){
        super(message);
    }
}
