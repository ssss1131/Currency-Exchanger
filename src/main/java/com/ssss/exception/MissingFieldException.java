package com.ssss.exception;

public class MissingFieldException extends RuntimeException{
    public MissingFieldException(String message){
        super(message);
    }
}
