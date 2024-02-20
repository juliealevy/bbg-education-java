package com.play.java.bbgeducation.application.sessions.update;

import br.com.fluentvalidator.AbstractValidator;
import br.com.fluentvalidator.predicate.StringPredicate;
import com.play.java.bbgeducation.application.common.commands.CommandValidator;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.application.common.validation.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static br.com.fluentvalidator.predicate.StringPredicate.stringEmptyOrNull;
import static br.com.fluentvalidator.predicate.StringPredicate.stringSizeBetween;
import static java.util.function.Predicate.not;

@Component
public class SessionUpdateCommandValidator extends AbstractValidator<SessionUpdateCommand>
    implements CommandValidator<SessionUpdateCommand, OneOf3<Success, NotFound, ValidationFailed>> {

    private final String entityName = "session";
    @Override
    public void rules() {
        setPropertyOnContext("session");

        ruleFor(SessionUpdateCommand::getName)
                .must(not(stringEmptyOrNull()))
                .withMessage(ValidationMessages.EmptyName(entityName))
                .withFieldName("name")
                .withAttempedValue(SessionUpdateCommand::getName)
                .must(stringSizeBetween(ValidationLengths.NameMin(), ValidationLengths.NameMax()))
                .withMessage(ValidationMessages.NameLength(entityName))
                .withFieldName("name")
                .withAttempedValue(SessionUpdateCommand::getName);

        ruleFor(SessionUpdateCommand::getDescription)
                .must(StringPredicate.stringSizeLessThan(ValidationLengths.DescriptionMax()))
                .withMessage(ValidationMessages.DescriptionLength(entityName))
                .withFieldName("description")
                .withAttempedValue(SessionUpdateCommand::getDescription);

        ruleFor(SessionUpdateCommand::getEndDate)
                .must(this::checkEndDateConstraint)
                .withMessage(ValidationMessages.EndAfterStartDate(entityName))
                .withFieldName("end date");
    }

    private boolean checkEndDateConstraint(final LocalDate endDate) {
        return endDate.isAfter(getPropertyOnContext(entityName, SessionUpdateCommand.class).getStartDate());
    }
    @Override
    public OneOfResultInfo getResultInfo() {
        return OneOfResultInfo.builder()
                .resultType(OneOf3.class)
                .validationFailedOptionNumber(3)
                .build();
    }
}
