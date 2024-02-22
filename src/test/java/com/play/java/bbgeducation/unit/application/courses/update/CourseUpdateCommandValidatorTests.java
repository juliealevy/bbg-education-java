package com.play.java.bbgeducation.unit.application.courses.update;

import br.com.fluentvalidator.Validator;
import br.com.fluentvalidator.context.ValidationResult;
import com.play.java.bbgeducation.application.courses.update.CourseUpdateCommand;
import com.play.java.bbgeducation.application.courses.update.CourseUpdateCommandValidator;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

public class CourseUpdateCommandValidatorTests {
    private final Validator<CourseUpdateCommand> underTest = new CourseUpdateCommandValidator();
    
    @Test    
    public void validate_ShouldReturnValid_WhenInputValid(){
        CourseUpdateCommand courseUpdateCommand = buildCommand();
        ValidationResult result = underTest.validate(courseUpdateCommand);

        assertThat(result).isNotNull();
        assertThat(result.isValid()).isTrue();
    }

    @Test
    public void validate_ShouldReturn2Errors_WhenNameEmpty(){
        validateInvalidInput(0, null, 2);
    }

    @Test
    public void validate_ShouldReturn1Error_WhenNameTooShort(){
        validateInvalidInput(2, null, 1);
    }

    @Test
    public void validate_ShouldReturnInvalid_WhenNameTooLong(){
        validateInvalidInput(100, null, 1);
    }

    @Test
    public void validate_ShouldReturnInvalid_WhenDescriptionTooLong(){
        validateInvalidInput(null, 300, 1);
    }

    private CourseUpdateCommand buildCommand(Integer nameLength, Integer descriptionLength){
        int nameSize = nameLength == null? 25: nameLength;
        int descSize = descriptionLength == null? 100: descriptionLength;

        return Instancio.of(CourseUpdateCommand.class)
                .generate(field(CourseUpdateCommand::getName), gen -> gen.string().minLength(nameSize).maxLength(nameSize))
                .generate(field(CourseUpdateCommand::getDescription), gen -> gen.string().minLength(descSize).maxLength(descSize))
                .create();
    }

    private void validateInvalidInput(Integer nameLength, Integer descriptionLength, int errorCount){
        CourseUpdateCommand CourseUpdateCommand = buildCommand(nameLength, descriptionLength);
        ValidationResult result = underTest.validate(CourseUpdateCommand);

        assertThat(result).isNotNull();
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrors()).isNotNull();
        assertThat(result.getErrors().size()).isEqualTo(errorCount);
    }
    private CourseUpdateCommand buildCommand(){
        return buildCommand(null,null);
    }
}
