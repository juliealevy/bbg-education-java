package com.play.java.bbgeducation.application.sessions.update;

import com.play.java.bbgeducation.application.common.commands.CommandValidator;
import com.play.java.bbgeducation.application.common.commands.EntityCommandValidator;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.application.common.validation.OneOfResultInfo;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.common.commands.ValidationMessages;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class SessionUpdateCommandValidator extends EntityCommandValidator<SessionUpdateCommand>
    implements CommandValidator<SessionUpdateCommand, OneOf3<Success, NotFound, ValidationFailed>> {

    @Override
    protected String getEntityName() {
        return "session";
    }

    @Override
    public void rules() {
        setPropertyOnContext("session");
        super.rules();
        ruleFor(SessionUpdateCommand::getEndDate)
                .must(this::checkEndDateConstraint)
                .withMessage(ValidationMessages.EndAfterStartDate(getEntityName()))
                .withFieldName("end date");
    }

    private boolean checkEndDateConstraint(final LocalDate endDate) {
        return endDate.isAfter(getPropertyOnContext(getEntityName(), SessionUpdateCommand.class).getStartDate());
    }
    @Override
    public OneOfResultInfo getResultInfo() {
        return OneOfResultInfo.builder()
                .resultType(OneOf3.class)
                .validationFailedOptionNumber(3)
                .build();
    }
}
