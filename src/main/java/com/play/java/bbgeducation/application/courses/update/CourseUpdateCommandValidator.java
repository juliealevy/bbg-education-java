package com.play.java.bbgeducation.application.courses.update;

import com.play.java.bbgeducation.application.common.commands.CommandValidator;
import com.play.java.bbgeducation.application.common.commands.EntityCommandValidator;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.application.common.validation.OneOfResultInfo;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;

public class CourseUpdateCommandValidator extends EntityCommandValidator<CourseUpdateCommand>
        implements CommandValidator<CourseUpdateCommand, OneOf3<Success, NotFound, ValidationFailed>> {

    @Override
    public void rules() {
        super.rules();
    }

    @Override
    public OneOfResultInfo getResultInfo() {
        return OneOfResultInfo.builder()
                .resultType(OneOf3.class)
                .validationFailedOptionNumber(3)
                .build();
    }

    @Override
    protected String getEntityName() {
        return "course";
    }
}
