package com.play.java.bbgeducation.application.common.exceptions.validation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValidationError {
    private String propertyName;
    private String errorMessage;
}
