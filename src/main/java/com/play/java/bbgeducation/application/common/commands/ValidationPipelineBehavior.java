package com.play.java.bbgeducation.application.common.commands;

import an.awesome.pipelinr.Command;
import br.com.fluentvalidator.context.Error;
import br.com.fluentvalidator.context.ValidationResult;
import com.play.java.bbgeducation.application.common.commands.CommandValidator;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.validation.OneOfResultInfo;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ValidationPipelineBehavior implements Command.Middleware {

    private final ObjectProvider<CommandValidator> validators;

    public ValidationPipelineBehavior(ObjectProvider<CommandValidator> validators) {
        this.validators = validators;
    }

    @Override
    public <R, C extends Command<R>> R invoke(C command, Next<R> next) {
        if (validators == null) {
            return next.invoke();
        }

        //look for a validator for the command (shouldn't be more than one)
        //if ever there is a use case to have more than one, need to update this
        Optional<CommandValidator> matchedValidator = validators.stream()
                .filter(v -> v.matches(command))
                .findFirst();

        //no validator for the command
        if (matchedValidator.isEmpty()) {
            return next.invoke();
        }

        //call for the validation
        ValidationResult validationResult = matchedValidator.get()
                .validate(command);

        //no validation issues/errors
        if (validationResult.isValid()) {
            return next.invoke();
        }

        //still, make sure there are errors to return, if not, throw an exception
        if (validationResult.getErrors() == null || validationResult.getErrors().size() == 0) {
            throw new RuntimeException("Unknown validation error occurred");
        }

        //return the errors
        List<Error> errorList = new ArrayList(validationResult.getErrors());
        OneOfResultInfo resultInfo = matchedValidator.get().getResultInfo();

        if (resultInfo.getResultType() == OneOf2.class) {
            return  (R) OneOf2.fromOptionNumber(resultInfo.getValidationFailedOptionNumber(), ValidationFailed.Conflict(errorList));
        }else if (resultInfo.getResultType() == OneOf3.class){
            return (R) OneOf3.fromOptionNumber(resultInfo.getValidationFailedOptionNumber(), ValidationFailed.Conflict(errorList));
        }else{
            throw new RuntimeException("Invalid validation result object type");
        }
    }
}


