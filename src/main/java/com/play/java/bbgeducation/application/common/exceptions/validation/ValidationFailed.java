package com.play.java.bbgeducation.application.common.exceptions.validation;

import br.com.fluentvalidator.context.Error;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValidationFailed {
    private ValidationErrorType errorType;
    private List<Error> errors;

    public ValidationFailed(ValidationErrorType errorType, String propertyName, String errorMessage){
        this.errorType = errorType;
        this.errors = Collections.singletonList(buildError(propertyName, errorMessage));
    }

   public static ValidationFailed BadRequest(String propertyName, String message){
        return ValidationFailed.builder()
                .errorType(ValidationErrorType.BadRequest)
                .errors(Collections.singletonList(buildError(propertyName, message)))
                .build();
    }

    public static ValidationFailed Conflict(String propertyName, String message){
        return ValidationFailed.builder()
                .errorType(ValidationErrorType.Conflict)
                .errors(Collections.singletonList(buildError(propertyName, message)))
                .build();
    }

    public static ValidationFailed Conflict(Collection<Error> errors){
        return ValidationFailed.builder()
                .errorType(ValidationErrorType.Conflict)
                .errors(errors.stream().toList())
                .build();
    }

    public static ValidationFailed BadRequest(Collection<Error> errors){
        return ValidationFailed.builder()
                .errorType(ValidationErrorType.BadRequest)
                .errors(errors.stream().toList())
                .build();
    }

    public Error getFirstError(){
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
            messages.append(e.getMessage()).append(System.lineSeparator());
        });
        return messages.toString();
    }

    private static Error buildError(String propertyName, String message){
        return Error.create(propertyName, message, "", null);
    }
}

