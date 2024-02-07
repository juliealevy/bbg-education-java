package com.play.java.bbgeducation.integration.programs.commandtests;

import an.awesome.pipelinr.Pipeline;
import com.play.java.bbgeducation.application.common.exceptions.validation.ValidationFailed;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.programs.ProgramResult;
import com.play.java.bbgeducation.application.programs.create.ProgramCreateCommand;
import com.play.java.bbgeducation.application.programs.commands.ProgramGetAllCommand;
import com.play.java.bbgeducation.integration.programs.DataUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProgramGetAllCommandHandlerTests {
    private final Pipeline underTest;

    @Autowired
    public ProgramGetAllCommandHandlerTests(Pipeline pipeline){
        this.underTest = pipeline;
    }

    @Test
    public void GetAllHandler_ShouldReturnAll() {
        ProgramCreateCommand createCmd1 = DataUtils.buildCreateCommandI();
        OneOf2<ProgramResult, ValidationFailed> saved1 = underTest.send(createCmd1);
        ProgramCreateCommand createCmd2 = DataUtils.buildCreateCommandII();
        OneOf2<ProgramResult, ValidationFailed> saved2 = underTest.send(createCmd2);

        List<ProgramResult> programs = underTest.send(ProgramGetAllCommand.builder().build());

        assertThat(programs).hasSize(2);
    }

    @Test
    public void GetAllHandler_CanReturnZero() {
        List<ProgramResult> programs = underTest.send(ProgramGetAllCommand.builder().build());

        assertThat(programs).hasSize(0);
    }

}
