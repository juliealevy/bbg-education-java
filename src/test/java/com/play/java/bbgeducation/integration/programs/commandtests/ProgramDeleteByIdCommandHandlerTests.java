package com.play.java.bbgeducation.integration.programs.commandtests;

import an.awesome.pipelinr.Pipeline;
import com.play.java.bbgeducation.application.programs.ProgramResult;
import com.play.java.bbgeducation.application.programs.commands.ProgramCreateCommand;
import com.play.java.bbgeducation.application.programs.commands.ProgramDeleteByIdCommand;
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
public class ProgramDeleteByIdCommandHandlerTests {
    private final Pipeline underTest;

    @Autowired
    public ProgramDeleteByIdCommandHandlerTests(Pipeline pipeline){
        this.underTest = pipeline;
    }

    //TODO:  update these when deletecommand updates return

    @Test
    public void DeleteByIdHandler_ShouldDeletedProgram_WhenIdValid() {
        ProgramCreateCommand createCmd1 = DataUtils.buildCreateCommandI();
        ProgramResult saved1 = underTest.send(createCmd1);

        ProgramDeleteByIdCommand deleteCommand = ProgramDeleteByIdCommand.builder()
                .id(saved1.getId())
                .build();
        underTest.send(deleteCommand);

        ProgramGetByIdCommand getByIdCommand = ProgramGetByIdCommand.builder().id(saved1.getId()).build();
        Optional<ProgramResult> checkForDeleted = underTest.send(getByIdCommand);

        assertThat(checkForDeleted).isEmpty();

    }

    @Test
    public void DeleteByIdHandler_ShouldReturnEmpty_WhenIdInvalid() {
        ProgramCreateCommand createCmd1 = DataUtils.buildCreateCommandI();
        ProgramResult saved1 = underTest.send(createCmd1);

        ProgramDeleteByIdCommand programCommand = ProgramDeleteByIdCommand.builder()
                .id(saved1.getId() + 100L)
                .build();

        Optional<ProgramResult> programResult = underTest.send(programCommand);

        assertThat(programResult).isEmpty();
    }


}
