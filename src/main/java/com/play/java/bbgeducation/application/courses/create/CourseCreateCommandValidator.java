package com.play.java.bbgeducation.application.courses.create;


import com.play.java.bbgeducation.application.common.commands.CommandValidator;
import com.play.java.bbgeducation.application.common.commands.EntityCommandValidator;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.validation.OneOfResultInfo;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.courses.results.CourseResult;
import org.springframework.stereotype.Component;

@Component
public class CourseCreateCommandValidator extends EntityCommandValidator<CourseCreateCommand>
    implements CommandValidator<CourseCreateCommand, OneOf2<CourseResult, ValidationFailed>>
{

    @Override
    protected String getEntityName() {
        return "course";
    }

    @Override
    public void rules() {
        super.rules();
    }

    @Override
    public OneOfResultInfo getResultInfo() {
        return OneOfResultInfo.builder()
                .resultType(OneOf2.class)
                .validationFailedOptionNumber(2)
                .build();
    }
}
