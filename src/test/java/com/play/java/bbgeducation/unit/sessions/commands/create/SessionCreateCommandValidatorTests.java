package com.play.java.bbgeducation.unit.sessions.commands.create;

import br.com.fluentvalidator.Validator;
import br.com.fluentvalidator.context.ValidationResult;
import com.play.java.bbgeducation.application.sessions.create.SessionCreateCommand;
import com.play.java.bbgeducation.application.sessions.create.SessionCreateCommandValidator;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class SessionCreateCommandValidatorTests {

    private final Validator<SessionCreateCommand> underTest = new SessionCreateCommandValidator();

    @Test
    public void validate_ShouldReturnValid_InputValid(){
        SessionCreateCommand sessionCreateCmd = buildCreateCommand();
        ValidationResult result = underTest.validate(sessionCreateCmd);

        assertThat(result).isNotNull();
        assertThat(result.isValid()).isTrue();
    }

    @Test
    public void validate_ShouldReturn2Errors_WhenNameEmpty(){
        SessionCreateCommand sessionCreateCmd = buildCreateCommand();
        sessionCreateCmd.setName("");

        ValidationResult result = underTest.validate(sessionCreateCmd);

        assertThat(result).isNotNull();
        assertFalse(result.isValid());
        assertThat(result.getErrors()).isNotNull();
        assertThat(result.getErrors().size()).isEqualTo(2);
    }

    @Test
    public void validate_ShouldReturnError_WhenNameTooShort(){
        SessionCreateCommand sessionCreateCmd = buildCreateCommand(2, null);

        ValidationResult result = underTest.validate(sessionCreateCmd);

        assertThat(result).isNotNull();
        assertFalse(result.isValid());
        assertThat(result.getErrors()).isNotNull();
        assertThat(result.getErrors().size()).isEqualTo(1);
    }

    @Test
    public void validate_ShouldReturnError_WhenNameTooLong(){
        SessionCreateCommand sessionCreateCmd = buildCreateCommand(200, null);

        ValidationResult result = underTest.validate(sessionCreateCmd);

        assertThat(result).isNotNull();
        assertFalse(result.isValid());
        assertThat(result.getErrors()).isNotNull();
        assertThat(result.getErrors().size()).isEqualTo(1);
    }

    @Test
    public void validate_ShouldReturnError_WhenDescTooLong(){
        SessionCreateCommand sessionCreateCmd = buildCreateCommand(null, 300);

        ValidationResult result = underTest.validate(sessionCreateCmd);

        assertThat(result).isNotNull();
        assertFalse(result.isValid());
        assertThat(result.getErrors()).isNotNull();
        assertThat(result.getErrors().size()).isEqualTo(1);
    }

    @Test
    public void validate_ShouldReturnError_WhenEndDateBeforeStartDate(){
        SessionCreateCommand sessionCreateCmd = buildCreateCommand();
        sessionCreateCmd.setEndDate(sessionCreateCmd.getStartDate().minusMonths(1));

        ValidationResult result = underTest.validate(sessionCreateCmd);

        assertThat(result).isNotNull();
        assertFalse(result.isValid());
        assertThat(result.getErrors()).isNotNull();
        assertThat(result.getErrors().size()).isEqualTo(1);
    }

    @Test
    public void validate_ShouldReturnError_WhenEndDateEqualsStartDate(){
        SessionCreateCommand sessionCreateCmd = buildCreateCommand();
        sessionCreateCmd.setEndDate(sessionCreateCmd.getStartDate());

        ValidationResult result = underTest.validate(sessionCreateCmd);

        assertThat(result).isNotNull();
        assertFalse(result.isValid());
        assertThat(result.getErrors()).isNotNull();
        assertThat(result.getErrors().size()).isEqualTo(1);
    }



    private SessionCreateCommand buildCreateCommand(Integer nameLength, Integer descriptionLength){
        int nameSize = nameLength == null? 25: nameLength;
        int descSize = descriptionLength == null? 100: descriptionLength;

        SessionCreateCommand cmd = Instancio.of(SessionCreateCommand.class)
                .generate(field("name"), gen -> gen.string().minLength(nameSize).maxLength(nameSize))
                .generate(field("description"), gen -> gen.string().minLength(descSize).maxLength(descSize))
                .create();

        cmd.setStartDate(LocalDate.now().plusMonths(1));
        cmd.setEndDate(cmd.getStartDate().plusMonths(7));
        cmd.setPracticumHours(Instancio.create(int.class));   //no limits right now

        return cmd;
    }

    private SessionCreateCommand buildCreateCommand(){
        return buildCreateCommand(null, null);
    }
}
