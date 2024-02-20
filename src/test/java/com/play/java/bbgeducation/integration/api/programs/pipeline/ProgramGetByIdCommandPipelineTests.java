package com.play.java.bbgeducation.integration.api.programs.pipeline;

import an.awesome.pipelinr.Pipeline;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.programs.result.ProgramResult;
import com.play.java.bbgeducation.application.programs.create.ProgramCreateCommand;
import com.play.java.bbgeducation.application.programs.getById.ProgramGetByIdCommand;
import com.play.java.bbgeducation.integration.api.programs.DataUtils;
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
public class ProgramGetByIdCommandPipelineTests {
    private final Pipeline underTest;

    @Autowired
    public ProgramGetByIdCommandPipelineTests(Pipeline pipeline){
        this.underTest = pipeline;
    }

    @Test
    public void GetByIdHandler_ShouldReturnProgram_WhenIdValid() {
        ProgramCreateCommand createCmd1 = DataUtils.buildCreateCommandI();
        OneOf2<ProgramResult, ValidationFailed> saved1 = underTest.send(createCmd1);

        ProgramGetByIdCommand programCommand = ProgramGetByIdCommand.builder()
                .id(saved1.asOption1().getId())
                .build();

        OneOf2<ProgramResult, NotFound> result = underTest.send(programCommand);

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

        OneOf2<ProgramResult, NotFound> result = underTest.send(programCommand);

        assertThat(result).isNotNull();
        assertThat(result.hasOption2()).isTrue();
    }


}
