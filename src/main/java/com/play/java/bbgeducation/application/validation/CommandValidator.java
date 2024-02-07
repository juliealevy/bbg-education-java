package com.play.java.bbgeducation.application.validation;

import an.awesome.pipelinr.Command;
import br.com.fluentvalidator.Validator;
import br.com.fluentvalidator.context.ValidationResult;
import com.google.common.reflect.TypeToken;

public interface CommandValidator<C extends Command<R>, R>  {

    //this returns ValidationResult which is part of the fluent validation library
    //and will be implemented in the AbstractValidator class
    ValidationResult validate(C command);

    default boolean matches(C command) {
        TypeToken<C> typeToken = new TypeToken<C>(getClass()) { // available in Guava 12+.
        };


        return typeToken.isSupertypeOf(command.getClass());
    }
}
