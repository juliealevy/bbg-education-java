package com.play.java.bbgeducation.application.programs.create;

import br.com.fluentvalidator.AbstractValidator;
import com.play.java.bbgeducation.application.common.exceptions.validation.ValidationFailed;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.programs.ProgramResult;
import com.play.java.bbgeducation.application.validation.CommandValidator;
import com.play.java.bbgeducation.application.validation.OneOfResultInfo;
import org.springframework.stereotype.Component;

import static br.com.fluentvalidator.predicate.StringPredicate.stringEmptyOrNull;
import static br.com.fluentvalidator.predicate.StringPredicate.stringSizeBetween;
import static java.util.function.Predicate.not;

//using two different libraries - one for validation and one for pipeline middleware which
//assumes you are not using another library for validation.
//extending AbstractValidator which does the fluent validation
//implementing CommandValidator which matches types of the pipeline middleware invoke, etc. so
//I can type check to match up command to validator in the middleware.

@Component
public class ProgramCreateCommandValidator
    extends AbstractValidator<ProgramCreateCommand>
        implements CommandValidator<ProgramCreateCommand, OneOf2<ProgramResult, ValidationFailed>>

{


    @Override
    public void rules() {

        ruleFor(ProgramCreateCommand::getName)
                .must(not(stringEmptyOrNull()))
                    .withMessage("Program name cannot be empty")
                    .withFieldName("name")
                .withAttempedValue(ProgramCreateCommand::getName)
                .must(stringSizeBetween(3, 50))
                    .withMessage("Program name must be between 3 and 50 characters.")
                    .withFieldName("name")
                    .withAttempedValue(ProgramCreateCommand::getName);

    }


    @Override
    public OneOfResultInfo getResultInfo() {
        return OneOfResultInfo.builder()
                .resultType(OneOf2.class)
                .validationFailedOptionNumber(2)
                .build();
    }
}
