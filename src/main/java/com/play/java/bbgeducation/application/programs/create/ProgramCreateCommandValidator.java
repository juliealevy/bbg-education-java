package com.play.java.bbgeducation.application.programs.create;

import br.com.fluentvalidator.AbstractValidator;
import org.springframework.stereotype.Component;

import static br.com.fluentvalidator.predicate.StringPredicate.stringEmptyOrNull;
import static br.com.fluentvalidator.predicate.StringPredicate.stringSizeBetween;
import static java.util.function.Predicate.not;


@Component
public class ProgramCreateCommandValidator extends AbstractValidator<ProgramCreateCommand> {

    @Override
    public void rules() {
        String nameMessage = "Program name must be between 3 and 50 characters.";

        ruleFor(ProgramCreateCommand::getName)
                .must(not(stringEmptyOrNull()))
                    .withMessage(nameMessage)
                    .withFieldName("name")
                .must(stringSizeBetween(3, 50))
                    .withMessage(nameMessage)
                    .withFieldName("name")
                .critical();
    }
}
