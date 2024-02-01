package com.play.java.bbgeducation.integration.programs.commandtests;

import an.awesome.pipelinr.Pipeline;
import com.play.java.bbgeducation.application.OneOf2;
import com.play.java.bbgeducation.application.exceptions.NameExistsException;
import com.play.java.bbgeducation.application.exceptions.NameExistsValidationFailed;
import com.play.java.bbgeducation.application.exceptions.ValidationFailed;
import com.play.java.bbgeducation.application.programs.*;
import com.play.java.bbgeducation.application.programs.commands.ProgramCreateCommand;
import com.play.java.bbgeducation.integration.programs.DataUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProgramCreateCommandHandlerTests {
    private final Pipeline underTest;

    @Autowired
    public ProgramCreateCommandHandlerTests(Pipeline pipeline){
        this.underTest = pipeline;
    }

    @Test
    public void CreateHandler_ShouldSucceed_WhenInputValid() {
        ProgramCreateCommand command = DataUtils.buildCreateCommandI();
        OneOf2<ProgramResult, ValidationFailed> saved = underTest.send(command);

        assertThat(saved).isNotNull();
        assertThat(saved.hasOption1()).isTrue();
        assertThat(saved.asOption1().getName()).isEqualTo(command.getName());
        assertThat(saved.asOption1().getDescription()).isEqualTo(command.getDescription());

    }

    @Test
    public void CreateHandler_ShouldFail_WhenNameExists() {
        ProgramCreateCommand command = DataUtils.buildCreateCommandI();
        OneOf2<ProgramResult, ValidationFailed> saved = underTest.send(command);
        OneOf2<ProgramResult, ValidationFailed> saved2 = underTest.send(command);

        assertThat(saved2).isNotNull();
        assertThat(saved2.hasOption2()).isTrue();
        assertThat(saved2.asOption2()).isNotNull();
        assertThat(saved2.asOption2().getErrors()).hasSize(1);
        assertThat(saved2.asOption2() instanceof NameExistsValidationFailed).isTrue();
    }

    //TODO  add validation for empty name, min/max lengths

}
