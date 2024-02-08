package com.play.java.bbgeducation.application.common.validation;

public class EmailExistsValidationFailed extends ValidationFailed{
    public EmailExistsValidationFailed(){
        super(ValidationErrorType.Conflict, "Email", " User email already exists");
    }
}
