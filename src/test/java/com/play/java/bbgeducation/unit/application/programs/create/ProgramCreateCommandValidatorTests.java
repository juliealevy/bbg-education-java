package com.play.java.bbgeducation.unit.application.programs.create;

import br.com.fluentvalidator.Validator;
import br.com.fluentvalidator.context.ValidationResult;
import com.play.java.bbgeducation.application.programs.create.ProgramCreateCommand;
import com.play.java.bbgeducation.application.programs.create.ProgramCreateCommandValidator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ProgramCreateCommandValidatorTests {

    private final Validator<ProgramCreateCommand> underTest = new ProgramCreateCommandValidator();

    @Test
    public void validate_ShouldReturnValid_InputValid(){
        ProgramCreateCommand cmd = ProgramCreateCommand.builder()
                .name("Program123")
                .description("Valid test")
                .build();

        ValidationResult result = underTest.validate(cmd);

        assertThat(result).isNotNull();
        assertThat(result.isValid()).isTrue();
    }

    @Test
    public void validate_ShouldReturnInvalid_WhenNameIsEmpty(){
        ProgramCreateCommand cmd = ProgramCreateCommand.builder()
                .name("")
                .description("Empty test")
                .build();


        ValidationResult result = underTest.validate(cmd);

        assertThat(result).isNotNull();
        assertFalse(result.isValid());
        assertThat(result.getErrors()).isNotNull();
        assertThat(result.getErrors().size()).isEqualTo(2);
    }

    @Test
    public void validate_ShouldReturnInvalid_WhenNameIsTooShort(){
        ProgramCreateCommand cmd = ProgramCreateCommand.builder()
                .name("ab")
                .description("too short test")
                .build();


        ValidationResult result = underTest.validate(cmd);

        assertThat(result).isNotNull();
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrors()).isNotNull();
        assertThat(result.getErrors().size()).isGreaterThan(0);

    }

    @Test
    public void validate_ShouldReturnInvalid_WhenNameIsTooLong(){
        ProgramCreateCommand cmd = ProgramCreateCommand.builder()
                .name("abcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdezzz")
                .description("too long test")
                .build();


        ValidationResult result = underTest.validate(cmd);

        assertThat(result).isNotNull();
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrors()).isNotNull();
        assertThat(result.getErrors().size()).isGreaterThan(0);

    }
}
