package com.play.java.bbgeducation.unit.sessions.commands.update;

import br.com.fluentvalidator.Validator;
import br.com.fluentvalidator.context.ValidationResult;
import com.play.java.bbgeducation.application.sessions.update.SessionUpdateCommand;
import com.play.java.bbgeducation.application.sessions.update.SessionUpdateCommandValidator;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ProgramSessionUpdateCommandValidatorTests {
    private final Validator<SessionUpdateCommand> underTest = new SessionUpdateCommandValidator();

    @Test
    public void validate_ShouldReturnValid_InputValid(){
        SessionUpdateCommand command = buildUpdateCommand();
        ValidationResult result = underTest.validate(command);

        assertThat(result).isNotNull();
        assertThat(result.isValid()).isTrue();
    }

    @Test
    public void validate_ShouldReturnError_ProgramMissing(){
        SessionUpdateCommand command = buildUpdateCommand();
        command.setProgramId(null);
        ValidationResult result = underTest.validate(command);

        assertThat(result).isNotNull();
        assertFalse(result.isValid());
        assertThat(result.getErrors()).isNotNull();
        assertThat(result.getErrors().size()).isEqualTo(1);
    }

    @Test
    public void validate_ShouldReturnError_SessionIdMissing(){
        SessionUpdateCommand command = buildUpdateCommand();
        command.setId(null);
        ValidationResult result = underTest.validate(command);

        assertThat(result).isNotNull();
        assertFalse(result.isValid());
        assertThat(result.getErrors()).isNotNull();
        assertThat(result.getErrors().size()).isEqualTo(1);
    }

    @Test
    public void validate_ShouldReturn2Errors_BothIdsMissing(){
        SessionUpdateCommand command = buildUpdateCommand();
        command.setId(null);
        command.setProgramId(null);
        ValidationResult result = underTest.validate(command);

        assertThat(result).isNotNull();
        assertFalse(result.isValid());
        assertThat(result.getErrors()).isNotNull();
        assertThat(result.getErrors().size()).isEqualTo(2);
    }

    @Test
    public void validate_ShouldReturn2Errors_WhenNameEmpty(){
        testNameDescriptionErrors(0, null, 2);
    }

    @Test
    public void validate_ShouldReturnError_WhenNameTooShort(){
        testNameDescriptionErrors(2, null, 1);
    }

    @Test
    public void validate_ShouldReturnError_WhenNameTooLong(){
        testNameDescriptionErrors(200, null, 1);
    }

    @Test
    public void validate_ShouldReturnError_WhenDescTooLong(){
        testNameDescriptionErrors(null, 300, 1);
    }

    @Test
    public void validate_ShouldReturnError_WhenEndDateBeforeStartDate(){
        LocalDate now =LocalDate.now();
        testDateErrors(now.plusMonths(5), now.plusMonths(1));
    }

    @Test
    public void validate_ShouldReturnError_WhenEndDateIsStartDate(){
        LocalDate now =LocalDate.now();
        testDateErrors(now.plusMonths(5), now.plusMonths(5));

    }

    private void testNameDescriptionErrors(Integer nameLength, Integer descriptionLength, int expectedErrorCount){
        SessionUpdateCommand command = buildUpdateCommand(nameLength,descriptionLength);
        ValidationResult result = underTest.validate(command);

        assertThat(result).isNotNull();
        assertFalse(result.isValid());
        assertThat(result.getErrors()).isNotNull();
        assertThat(result.getErrors().size()).isEqualTo(expectedErrorCount);
    }

    private void testDateErrors(LocalDate startDate, LocalDate endDate){
        SessionUpdateCommand command = buildUpdateCommand();
        command.setStartDate(startDate);
        command.setEndDate(endDate);
        ValidationResult result = underTest.validate(command);

        assertThat(result).isNotNull();
        assertFalse(result.isValid());
        assertThat(result.getErrors()).isNotNull();
        assertThat(result.getErrors().size()).isEqualTo(1);
    }

    private SessionUpdateCommand buildUpdateCommand(Integer nameLength, Integer descriptionLength){
        int nameSize = nameLength == null? 25: nameLength;
        int descSize = descriptionLength == null? 100: descriptionLength;

        SessionUpdateCommand cmd = Instancio.of(SessionUpdateCommand.class)
                .generate(field("name"), gen -> gen.string().minLength(nameSize).maxLength(nameSize))
                .generate(field("description"), gen -> gen.string().minLength(descSize).maxLength(descSize))
                .create();

        cmd.setStartDate(LocalDate.now().plusMonths(1));
        cmd.setEndDate(cmd.getStartDate().plusMonths(7));
        cmd.setPracticumHours(Instancio.create(int.class));   //no limits right now

        return cmd;
    }

    private SessionUpdateCommand buildUpdateCommand(){
        return buildUpdateCommand(null, null);
    }
}
