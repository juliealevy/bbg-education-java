package com.play.java.bbgeducation.application.sessions.create;

import br.com.fluentvalidator.AbstractValidator;
import br.com.fluentvalidator.predicate.DatePredicate;
import br.com.fluentvalidator.predicate.StringPredicate;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.validation.CommandValidator;
import com.play.java.bbgeducation.application.common.validation.OneOfResultInfo;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.sessions.result.SessionResult;

import java.time.LocalDate;

import static br.com.fluentvalidator.predicate.StringPredicate.stringEmptyOrNull;
import static br.com.fluentvalidator.predicate.StringPredicate.stringSizeBetween;
import static java.util.function.Predicate.not;

public class SessionCreateCommandValidator extends AbstractValidator<SessionCreateCommand>
    implements CommandValidator<SessionCreateCommand, OneOf3<SessionResult, NotFound, ValidationFailed>>
{


    @Override
    public void rules() {

        setPropertyOnContext("session");

        ruleFor(SessionCreateCommand::getName)
                .must(not(stringEmptyOrNull()))
                .withMessage("Session name cannot be empty")
                .withFieldName("name")
                .withAttempedValue(SessionCreateCommand::getName)
                .must(stringSizeBetween(3, 50))
                .withMessage("Session name must be between 3 and 50 characters.")
                .withFieldName("name")
                .withAttempedValue(SessionCreateCommand::getName);

        ruleFor(SessionCreateCommand::getDescription)
                .must(StringPredicate.stringSizeLessThan(256))
                .withMessage("Session description must be less than 256 characters.")
                .withFieldName("description")
                .withAttempedValue(SessionCreateCommand::getDescription);

        ruleFor(SessionCreateCommand::getEndDate)
                .must(this::checkEndDateConstraint)
                .withMessage("Session end date must be after the start date.")
                .withFieldName("end date");
    }

    private boolean checkEndDateConstraint(final LocalDate endDate) {
        return endDate.isAfter(getPropertyOnContext("session", SessionCreateCommand.class).getStartDate());
    }

    @Override
    public OneOfResultInfo getResultInfo() {
        return OneOfResultInfo.builder()
                .resultType(OneOf3.class)
                .validationFailedOptionNumber(3)
                .build();
    }
}
