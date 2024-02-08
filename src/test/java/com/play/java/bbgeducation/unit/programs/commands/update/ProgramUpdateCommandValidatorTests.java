package com.play.java.bbgeducation.unit.programs.commands.update;

import br.com.fluentvalidator.context.ValidationResult;
import com.play.java.bbgeducation.application.programs.update.ProgramUpdateCommand;
import com.play.java.bbgeducation.application.programs.update.ProgramUpdateCommandValidator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProgramUpdateCommandValidatorTests {

    private final ProgramUpdateCommandValidator underTest = new ProgramUpdateCommandValidator();

    @Test
    public void validate_ShouldReturnValid_InputValid(){
        ProgramUpdateCommand cmd = ProgramUpdateCommand.builder()
                .id(1L)
                .name("Program123")
                .description("Valid test")
                .build();

        ValidationResult result = underTest.validate(cmd);

        assertThat(result).isNotNull();
        assertTrue(result.isValid());
    }

    @Test
    public void validate_ShouldReturnInvalid_WhenNameIsEmpty(){
        ProgramUpdateCommand cmd = ProgramUpdateCommand.builder()
                .id(1L)
                .name("")
                .description("Empty name test")
                .build();


        ValidationResult result = underTest.validate(cmd);

        assertThat(result).isNotNull();
        assertFalse(result.isValid());
        assertThat(result.getErrors()).isNotNull();
        assertThat(result.getErrors().size()).isEqualTo(2);
    }

    @Test
    public void validate_ShouldReturnInvalid_WhenNameIsTooShort(){
        ProgramUpdateCommand cmd = ProgramUpdateCommand.builder()
                .id(1L)
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
        ProgramUpdateCommand cmd = ProgramUpdateCommand.builder()
                .id(1L)
                .name("abcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdezzz")
                .description("too long test")
                .build();


        ValidationResult result = underTest.validate(cmd);

        assertThat(result).isNotNull();
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrors()).isNotNull();
        assertThat(result.getErrors().size()).isGreaterThan(0);

    }

    @Test
    public void validate_ShouldReturnInvalid_WhenIdZero(){
        ProgramUpdateCommand cmd = ProgramUpdateCommand.builder()
                .id(0L)
                .name("abcdea")
                .description("zero id test")
                .build();


        ValidationResult result = underTest.validate(cmd);

        assertThat(result).isNotNull();
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrors()).isNotNull();
        assertThat(result.getErrors().size()).isGreaterThan(0);

    }

    @Test
    public void validate_ShouldReturnInvalid_WhenNameIsEmptyAndIdZero(){
        ProgramUpdateCommand cmd = ProgramUpdateCommand.builder()
                .id(0L)
                .name("")
                .description("Empty name test")
                .build();


        ValidationResult result = underTest.validate(cmd);

        assertThat(result).isNotNull();
        assertFalse(result.isValid());
        assertThat(result.getErrors()).isNotNull();
        assertThat(result.getErrors().size()).isEqualTo(3);
    }

}
