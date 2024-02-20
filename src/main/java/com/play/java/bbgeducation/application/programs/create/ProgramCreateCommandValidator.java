package com.play.java.bbgeducation.application.programs.create;

import com.play.java.bbgeducation.application.common.commands.CommandValidator;
import com.play.java.bbgeducation.application.common.commands.EntityCommandValidator;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.validation.OneOfResultInfo;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.programs.result.ProgramResult;
import org.springframework.stereotype.Component;

//using two different libraries - one for validation and one for pipeline middleware which
//assumes you are not using another library for validation.
//extending AbstractValidator which does the fluent validation
//implementing CommandValidator which matches types of the pipeline middleware invoke, etc. so
//I can type check to match up command to validator in the middleware.

@Component
public class ProgramCreateCommandValidator
    extends EntityCommandValidator<ProgramCreateCommand>
        implements CommandValidator<ProgramCreateCommand, OneOf2<ProgramResult, ValidationFailed>>

{

    @Override
    protected String getEntityName() {
        return "program";
    }

    @Override
    public void rules() {
       super.rules();
       //add custom

    }


    @Override
    public OneOfResultInfo getResultInfo() {
        return OneOfResultInfo.builder()
                .resultType(OneOf2.class)
                .validationFailedOptionNumber(2)
                .build();
    }
}
