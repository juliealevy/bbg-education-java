package com.play.java.bbgeducation.application.sessions.create;

import com.play.java.bbgeducation.application.common.commands.CommandValidator;
import com.play.java.bbgeducation.application.common.commands.EntityCommandValidator;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.validation.OneOfResultInfo;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.common.commands.ValidationMessages;
import com.play.java.bbgeducation.application.sessions.result.SessionResult;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class SessionCreateCommandValidator extends EntityCommandValidator<SessionCreateCommand>
    implements CommandValidator<SessionCreateCommand, OneOf3<SessionResult, NotFound, ValidationFailed>>
{

    @Override
    protected String getEntityName() {
        return "session";
    }

    @Override
    public void rules() {
        setPropertyOnContext(getEntityName());
        super.rules();

        ruleFor(SessionCreateCommand::getEndDate)
                .must(this::checkEndDateConstraint)
                .withMessage(ValidationMessages.EndAfterStartDate(getEntityName()))
                .withFieldName("end date");
    }

    private boolean checkEndDateConstraint(final LocalDate endDate) {
        return endDate.isAfter(getPropertyOnContext(getEntityName(), SessionCreateCommand.class).getStartDate());
    }

    @Override
    public OneOfResultInfo getResultInfo() {
        return OneOfResultInfo.builder()
                .resultType(OneOf3.class)
                .validationFailedOptionNumber(3)
                .build();
    }
}
