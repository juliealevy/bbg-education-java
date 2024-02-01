package com.play.java.bbgeducation.application.exceptions;

public class NameExistsValidationFailed extends ValidationFailed {
    public NameExistsValidationFailed(String entityName){
        super(ValidationErrorType.Conflict, "Name", entityName + " name already exists");
    }
}
