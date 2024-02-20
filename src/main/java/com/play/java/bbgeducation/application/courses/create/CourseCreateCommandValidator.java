package com.play.java.bbgeducation.application.courses.create;


import br.com.fluentvalidator.AbstractValidator;
import br.com.fluentvalidator.predicate.StringPredicate;
import com.play.java.bbgeducation.application.common.commands.CommandValidator;
import com.play.java.bbgeducation.application.common.commands.EntityCommandValidator;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.validation.*;
import com.play.java.bbgeducation.application.courses.results.CourseResult;
import org.springframework.stereotype.Component;

import static br.com.fluentvalidator.predicate.StringPredicate.stringEmptyOrNull;
import static br.com.fluentvalidator.predicate.StringPredicate.stringSizeBetween;
import static java.util.function.Predicate.not;

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
