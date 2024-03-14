package com.play.java.bbgeducation.application.exceptions;

public class InvalidEmailFormatException extends RuntimeException{
    public InvalidEmailFormatException(String email){
        super(email + " is not a valid email address.");
    }
}
