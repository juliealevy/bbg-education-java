package com.play.java.bbgeducation.application.sessions.update;

import br.com.fluentvalidator.AbstractValidator;
import br.com.fluentvalidator.predicate.StringPredicate;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.application.common.validation.CommandValidator;
import com.play.java.bbgeducation.application.common.validation.OneOfResultInfo;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Objects;

import static br.com.fluentvalidator.predicate.StringPredicate.stringEmptyOrNull;
import static br.com.fluentvalidator.predicate.StringPredicate.stringSizeBetween;
import static java.util.function.Predicate.not;

@Component
public class SessionUpdateCommandValidator extends AbstractValidator<SessionUpdateCommand>
    implements CommandValidator<SessionUpdateCommand, OneOf3<Success, NotFound, ValidationFailed>> {

    @Override
    public void rules() {
        setPropertyOnContext("session");

        ruleFor(SessionUpdateCommand::getProgramId)
                .must(Objects::nonNull)
                .withMessage("Program Id must be provided.");
        ruleFor(SessionUpdateCommand::getId)
                .must(Objects::nonNull)
                .withMessage("Session Id must be provided.");

        ruleFor(SessionUpdateCommand::getName)
                .must(not(stringEmptyOrNull()))
                .withMessage("Session name cannot be empty")
                .withFieldName("name")
                .withAttempedValue(SessionUpdateCommand::getName)
                .must(stringSizeBetween(3, 50))
                .withMessage("Session name must be between 3 and 50 characters.")
                .withFieldName("name")
                .withAttempedValue(SessionUpdateCommand::getName);

        ruleFor(SessionUpdateCommand::getDescription)
                .must(StringPredicate.stringSizeLessThan(256))
                .withMessage("Session description must be less than 256 characters.")
                .withFieldName("description")
                .withAttempedValue(SessionUpdateCommand::getDescription);

        ruleFor(SessionUpdateCommand::getEndDate)
                .must(this::checkEndDateConstraint)
                .withMessage("Session end date must be after the start date.")
                .withFieldName("end date");
    }

    private boolean checkEndDateConstraint(final LocalDate endDate) {
        return endDate.isAfter(getPropertyOnContext("session", SessionUpdateCommand.class).getStartDate());
    }
    @Override
    public OneOfResultInfo getResultInfo() {
        return OneOfResultInfo.builder()
                .resultType(OneOf3.class)
                .validationFailedOptionNumber(3)
                .build();
    }
}
