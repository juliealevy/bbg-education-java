package com.play.java.bbgeducation.integration.programs.commandtests;

import an.awesome.pipelinr.Pipeline;
import com.play.java.bbgeducation.application.common.exceptions.validation.ValidationFailed;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.application.programs.ProgramResult;
import com.play.java.bbgeducation.application.programs.commands.ProgramCreateCommand;
import com.play.java.bbgeducation.application.programs.commands.ProgramDeleteByIdCommand;
import com.play.java.bbgeducation.integration.programs.DataUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
        OneOf2<ProgramResult, ValidationFailed> saved1 = underTest.send(createCmd1);

        ProgramDeleteByIdCommand deleteCommand = ProgramDeleteByIdCommand.builder()
                .id(saved1.asOption1().getId())
                .build();
        OneOf2<Success, NotFound> deleted = underTest.send(deleteCommand);

        assertThat(deleted).isNotNull();
        assertThat(deleted.hasOption1()).isTrue();

    }

    @Test
    public void DeleteByIdHandler_ShouldReturnNotFound_WhenIdInvalid() {
        ProgramCreateCommand createCmd1 = DataUtils.buildCreateCommandI();
        OneOf2<ProgramResult, ValidationFailed> saved1 = underTest.send(createCmd1);

        ProgramDeleteByIdCommand deleteCommand = ProgramDeleteByIdCommand.builder()
                .id(saved1.asOption1().getId() + 100L)
                .build();

        OneOf2<Success, NotFound> deleted = underTest.send(deleteCommand);

        assertThat(deleted).isNotNull();
        assertThat(deleted.hasOption2()).isTrue();
    }


}
