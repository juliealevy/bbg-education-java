package com.play.java.bbgeducation.application.common.validation;

import org.springframework.http.HttpStatus;

public enum ValidationErrorType{
    BadRequest(HttpStatus.BAD_REQUEST),
    Conflict(HttpStatus.CONFLICT),
    Unauthorized(HttpStatus.UNAUTHORIZED);

    public final HttpStatus value;

    private ValidationErrorType(HttpStatus httpStatus){
        this.value = httpStatus;
    }

}
