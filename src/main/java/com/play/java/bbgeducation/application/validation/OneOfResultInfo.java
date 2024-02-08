package com.play.java.bbgeducation.application.validation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OneOfResultInfo {
    private Object resultType;
    private int validationFailedOptionNumber;
    //can add more if needed
}
