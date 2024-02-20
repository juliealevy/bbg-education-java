package com.play.java.bbgeducation.application.courses.create;


import br.com.fluentvalidator.AbstractValidator;
import br.com.fluentvalidator.predicate.StringPredicate;
import com.play.java.bbgeducation.application.common.commands.CommandValidator;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.validation.*;
import com.play.java.bbgeducation.application.courses.results.CourseResult;
import org.springframework.stereotype.Component;

import static br.com.fluentvalidator.predicate.StringPredicate.stringEmptyOrNull;
import static br.com.fluentvalidator.predicate.StringPredicate.stringSizeBetween;
import static java.util.function.Predicate.not;

@Component
public class CourseCreateCommandValidator extends AbstractValidator<CourseCreateCommand>
    implements CommandValidator<CourseCreateCommand, OneOf2<CourseResult, ValidationFailed>>
{
    private final String entityName = "course";
    @Override
    public void rules() {
        ruleFor(CourseCreateCommand::getName)
                .must(not(stringEmptyOrNull()))
                .withMessage(ValidationMessages.EmptyName(entityName))
                .withFieldName("name")
                .withAttempedValue(CourseCreateCommand::getName)
                .must(stringSizeBetween(ValidationLengths.NameMin(), ValidationLengths.NameMax()))
                .withMessage(ValidationMessages.NameLength(entityName))
                .withFieldName("name")
                .withAttempedValue(CourseCreateCommand::getName);

        ruleFor(CourseCreateCommand::getDescription)
                .must(StringPredicate.stringSizeLessThan(ValidationLengths.DescriptionMax()))
                .withMessage(ValidationMessages.DescriptionLength(entityName))
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
