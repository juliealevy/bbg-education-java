package com.play.java.bbgeducation.application.common.validation;

public class NameExistsValidationFailed extends ValidationFailed {
    public NameExistsValidationFailed(String entityName){
        super(ValidationErrorType.Conflict, "Name", entityName + " name already exists");
    }
}
