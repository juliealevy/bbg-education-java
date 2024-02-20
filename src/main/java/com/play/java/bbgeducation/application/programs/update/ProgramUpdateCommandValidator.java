package com.play.java.bbgeducation.application.programs.update;

import com.play.java.bbgeducation.application.common.commands.CommandValidator;
import com.play.java.bbgeducation.application.common.commands.EntityCommandValidator;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.application.common.validation.OneOfResultInfo;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import org.springframework.stereotype.Component;

@Component
public class ProgramUpdateCommandValidator extends EntityCommandValidator<ProgramUpdateCommand>
    implements CommandValidator<ProgramUpdateCommand, OneOf3<Success, NotFound, ValidationFailed>>
{
    @Override
    protected String getEntityName() {
        return "program";
    }

    @Override
    public void rules() {
        super.rules();
        //custom here
    }

    @Override
    public OneOfResultInfo getResultInfo() {
        return OneOfResultInfo.builder()
                .resultType(OneOf3.class)
                .validationFailedOptionNumber(3)
                .build();
    }
}
