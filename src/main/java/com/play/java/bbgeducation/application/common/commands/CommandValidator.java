package com.play.java.bbgeducation.application.common.commands;

import an.awesome.pipelinr.Command;
import br.com.fluentvalidator.context.ValidationResult;
import com.google.common.reflect.TypeToken;
import com.play.java.bbgeducation.application.common.oneof.OneOf;
import com.play.java.bbgeducation.application.common.validation.OneOfResultInfo;

public interface CommandValidator<C extends Command<R>, R  extends OneOf>  {

    //this returns ValidationResult which is part of the fluent validation library
    //and will be implemented in the AbstractValidator class
    ValidationResult validate(C command);

    //to determine a match between command and validator
    default boolean matches(C command) {
        TypeToken<C> typeToken = new TypeToken<C>(getClass()) {
        };
        return typeToken.isSupertypeOf(command.getClass());
    }

    //to determine which OneOf class in the result and info about its options
    OneOfResultInfo getResultInfo();

}
