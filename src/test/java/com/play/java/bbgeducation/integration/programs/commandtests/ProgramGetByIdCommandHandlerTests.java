package com.play.java.bbgeducation.integration.programs.commandtests;

import an.awesome.pipelinr.Pipeline;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.exceptions.validation.ValidationFailed;
import com.play.java.bbgeducation.application.common.oneof.OneOfTypes;
import com.play.java.bbgeducation.application.programs.ProgramResult;
import com.play.java.bbgeducation.application.programs.commands.ProgramCreateCommand;
import com.play.java.bbgeducation.application.programs.commands.ProgramGetByIdCommand;
import com.play.java.bbgeducation.integration.programs.DataUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProgramGetByIdCommandHandlerTests {
    private final Pipeline underTest;

    @Autowired
    public ProgramGetByIdCommandHandlerTests(Pipeline pipeline){
        this.underTest = pipeline;
    }

    @Test
    public void GetByIdHandler_ShouldReturnProgram_WhenIdValid() {
        ProgramCreateCommand createCmd1 = DataUtils.buildCreateCommandI();
        OneOf2<ProgramResult, ValidationFailed> saved1 = underTest.send(createCmd1);

        ProgramGetByIdCommand programCommand = ProgramGetByIdCommand.builder()
                .id(saved1.asOption1().getId())
                .build();

        OneOf2<ProgramResult, OneOfTypes.NotFound> result = underTest.send(programCommand);

        assertThat(result).isNotNull();
        assertThat(result.hasOption1()).isTrue();
        assertThat(result.asOption1()).isEqualTo(saved1.asOption1());
    }

    @Test
    public void GetByIdHandler_ShouldReturnEmpty_WhenIdInValid() {
        ProgramCreateCommand createCmd1 = DataUtils.buildCreateCommandI();
        OneOf2<ProgramResult, ValidationFailed> saved1 = underTest.send(createCmd1);

        ProgramGetByIdCommand programCommand = ProgramGetByIdCommand.builder()
                .id(saved1.asOption1().getId() + 100L)
                .build();

        OneOf2<ProgramResult, OneOfTypes.NotFound> result = underTest.send(programCommand);

        assertThat(result).isNotNull();
        assertThat(result.hasOption2()).isTrue();
    }


}
