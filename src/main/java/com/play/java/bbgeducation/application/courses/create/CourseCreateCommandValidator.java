package com.play.java.bbgeducation.application.courses.create;


import br.com.fluentvalidator.AbstractValidator;
import br.com.fluentvalidator.predicate.StringPredicate;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.validation.CommandValidator;
import com.play.java.bbgeducation.application.common.validation.OneOfResultInfo;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.common.validation.ValidationMessages;
import com.play.java.bbgeducation.application.courses.results.CourseResult;
import org.springframework.stereotype.Component;

import static br.com.fluentvalidator.predicate.StringPredicate.stringEmptyOrNull;
import static br.com.fluentvalidator.predicate.StringPredicate.stringSizeBetween;
import static java.util.function.Predicate.not;

@Component
public class CourseCreateCommandValidator extends AbstractValidator<CourseCreateCommand>
    implements CommandValidator<CourseCreateCommand, OneOf2<CourseResult, ValidationFailed>>
{
    private final String ENTITY_NAME = "Course";
    @Override
    public void rules() {
        ruleFor(CourseCreateCommand::getName)
                .must(not(stringEmptyOrNull()))
                .withMessage(ValidationMessages.EmptyName(ENTITY_NAME))
                .withFieldName("name")
                .withAttempedValue(CourseCreateCommand::getName)
                .must(stringSizeBetween(3, 50))
                .withMessage(ValidationMessages.NameLength(ENTITY_NAME, 3, 50))
                .withFieldName("name")
                .withAttempedValue(CourseCreateCommand::getName);

        ruleFor(CourseCreateCommand::getDescription)
                .must(StringPredicate.stringSizeLessThan(256))
                .withMessage(ValidationMessages.DescriptionLength(ENTITY_NAME, 256))
                .withFieldName("description")
                .withAttempedValue(CourseCreateCommand::getDescription);

    }

    @Override
    public OneOfResultInfo getResultInfo() {
        return OneOfResultInfo.builder()
                .resultType(OneOf2.class)
                .validationFailedOptionNumber(2)
                .build();
    }
}
