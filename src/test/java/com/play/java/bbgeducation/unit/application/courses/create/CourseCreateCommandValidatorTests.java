package com.play.java.bbgeducation.unit.application.courses.create;

import br.com.fluentvalidator.Validator;
import br.com.fluentvalidator.context.ValidationResult;
import com.play.java.bbgeducation.application.courses.create.CourseCreateCommand;
import com.play.java.bbgeducation.application.courses.create.CourseCreateCommandValidator;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

public class CourseCreateCommandValidatorTests {
    private final Validator<CourseCreateCommand> underTest = new CourseCreateCommandValidator();

    @Test
    public void validate_ShouldReturnValid_WhenInputValid(){
        CourseCreateCommand courseCreateCommand = buildCommand();
        ValidationResult result = underTest.validate(courseCreateCommand);

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


    private CourseCreateCommand buildCommand(Integer nameLength, Integer descriptionLength){
        int nameSize = nameLength == null? 25: nameLength;
        int descSize = descriptionLength == null? 100: descriptionLength;

        return Instancio.of(CourseCreateCommand.class)
                .generate(field(CourseCreateCommand::getName), gen -> gen.string().minLength(nameSize).maxLength(nameSize))
                .generate(field(CourseCreateCommand::getDescription), gen -> gen.string().minLength(descSize).maxLength(descSize))
                .create();
    }

    private void validateInvalidInput(Integer nameLength, Integer descriptionLength, int errorCount){
        CourseCreateCommand courseCreateCommand = buildCommand(nameLength, descriptionLength);
        ValidationResult result = underTest.validate(courseCreateCommand);

        assertThat(result).isNotNull();
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrors()).isNotNull();
        assertThat(result.getErrors().size()).isEqualTo(errorCount);
    }
    private CourseCreateCommand buildCommand(){
        return buildCommand(null,null);
    }
}
