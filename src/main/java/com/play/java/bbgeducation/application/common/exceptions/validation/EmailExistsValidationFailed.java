package com.play.java.bbgeducation.application.common.exceptions.validation;

public class EmailExistsValidationFailed extends ValidationFailed{
    public EmailExistsValidationFailed(){
        super(ValidationErrorType.Conflict, "Email", " User email already exists");
    }
}
