package com.play.java.bbgeducation.application.programs.update;

import br.com.fluentvalidator.AbstractValidator;
import com.play.java.bbgeducation.application.common.exceptions.validation.ValidationFailed;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.application.programs.create.ProgramCreateCommand;
import com.play.java.bbgeducation.application.validation.CommandValidator;
import com.play.java.bbgeducation.application.validation.OneOfResultInfo;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static br.com.fluentvalidator.predicate.ComparablePredicate.greaterThan;
import static br.com.fluentvalidator.predicate.StringPredicate.stringEmptyOrNull;
import static br.com.fluentvalidator.predicate.StringPredicate.stringSizeBetween;
import static java.util.function.Predicate.not;

@Component
public class ProgramUpdateCommandValidator extends AbstractValidator<ProgramUpdateCommand>
    implements CommandValidator<ProgramUpdateCommand, OneOf3<Success, NotFound, ValidationFailed>>


{
    @Override
    public void rules() {
        ruleFor(ProgramUpdateCommand::getName)
                .must(not(stringEmptyOrNull()))
                .withMessage("Program name cannot be empty")
                .withFieldName("name")
                .withAttempedValue(ProgramUpdateCommand::getName)
                .must(stringSizeBetween(3, 50))
                .withMessage("Program name must be between 3 and 50 characters.")
                .withFieldName("name")
                .withAttempedValue(ProgramUpdateCommand::getName);

        ruleFor(ProgramUpdateCommand::getId)
                .must(not(Objects::isNull))
                .withMessage("Program Id cannot be empty")
                .must(greaterThan(0L))
                .withMessage("Program Id must be greater than 0")
                .withAttempedValue(ProgramUpdateCommand::getId);




    }

    @Override
    public OneOfResultInfo getResultInfo() {
        return OneOfResultInfo.builder()
                .resultType(OneOf3.class)
                .validationFailedOptionNumber(3)
                .build();
    }
}
