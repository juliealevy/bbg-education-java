package com.play.java.bbgeducation.application.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValidationFailed {
    private ValidationErrorType errorType;
    private List<ValidationError> errors;

    public ValidationFailed(ValidationErrorType errorType, String propertyName, String errorMessage){
        this.errorType = errorType;
        this.errors = Collections.singletonList(ValidationError.builder()
                        .propertyName(propertyName)
                        .errorMessage(errorMessage)
                        .build());
    }
    public static ValidationFailed BadRequest(String propertyName, String message){
        ValidationError error = ValidationError.builder()
                .propertyName(propertyName)
                .errorMessage(message)
                .build();

        return ValidationFailed.builder()
                .errorType(ValidationErrorType.BadRequest)
                .errors(Collections.singletonList(error))
                .build();
    }

    public static ValidationFailed Conflict(String propertyName, String message){
        ValidationError error = ValidationError.builder()
                .propertyName(propertyName)
                .errorMessage(message)
                .build();

        return ValidationFailed.builder()
                .errorType(ValidationErrorType.Conflict)
                .errors(Collections.singletonList(error))
                .build();
    }

    public static ValidationFailed BadRequest(List<ValidationError> errors){
        return ValidationFailed.builder()
                .errorType(ValidationErrorType.BadRequest)
                .errors(errors)
                .build();
    }

    public ValidationError getFirstError(){
        if (this.errors == null || this.errors.isEmpty()){
            return null;
        }
        return this.errors.stream().findFirst().get();
    }

    public String getErrorMessage(){
        if (this.errors == null || this.errors.isEmpty()){
            return "";
        }
        StringBuilder messages = new StringBuilder();
        this.errors.forEach(e -> {
            messages.append(e.getErrorMessage()).append(System.lineSeparator());
        });
        return messages.toString();
    }
}

