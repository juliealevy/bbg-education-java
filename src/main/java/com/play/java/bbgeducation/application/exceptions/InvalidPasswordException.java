package com.play.java.bbgeducation.application.exceptions;

public class InvalidPasswordException extends RuntimeException{
    public InvalidPasswordException(){
        super("Password is invalid and does not fulfill requirements.");
    }
}
