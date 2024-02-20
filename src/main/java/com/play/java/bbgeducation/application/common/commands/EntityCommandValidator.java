package com.play.java.bbgeducation.application.common.commands;

import br.com.fluentvalidator.AbstractValidator;
import br.com.fluentvalidator.predicate.StringPredicate;

import static br.com.fluentvalidator.predicate.StringPredicate.stringEmptyOrNull;
import static br.com.fluentvalidator.predicate.StringPredicate.stringSizeBetween;
import static java.util.function.Predicate.not;

public abstract class EntityCommandValidator<T extends EntityCommand> extends AbstractValidator<T> {

    protected abstract String getEntityName();

    @Override
    public void rules() {
        ruleFor(T::getName)
                .must(not(stringEmptyOrNull()))
                .withMessage(ValidationMessages.EmptyName(getEntityName()))
                .withFieldName("name")
                .withAttempedValue(T::getName)
                .must(stringSizeBetween(ValidationLengths.NameMin(), ValidationLengths.NameMax()))
                .withMessage(ValidationMessages.NameLength(getEntityName()))
                .withFieldName("name")
                .withAttempedValue(T::getName);

        ruleFor(T::getDescription)
                .must(StringPredicate.stringSizeLessThan(ValidationLengths.DescriptionMax()))
                .withMessage(ValidationMessages.DescriptionLength(getEntityName()))
                .withFieldName("description")
                .withAttempedValue(T::getDescription);
    }
}
