package com.play.java.bbgeducation.integration.api.programs.pipeline;

import an.awesome.pipelinr.Pipeline;
import com.play.java.bbgeducation.application.common.validation.NameExistsValidationFailed;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.application.programs.result.ProgramResult;
import com.play.java.bbgeducation.application.programs.create.ProgramCreateCommand;
import com.play.java.bbgeducation.application.programs.update.ProgramUpdateCommand;
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
public class ProgramUpdateCommandPipelineTests {
    private final Pipeline underTest;

    @Autowired
    public ProgramUpdateCommandPipelineTests(Pipeline pipeline){
        this.underTest = pipeline;
    }

    @Test
    public void UpdateHandler_ShouldSucceed_WhenInputValid() {
        ProgramCreateCommand createCommand = DataUtils.buildCreateCommandI();
        OneOf2<ProgramResult, ValidationFailed> saved = underTest.send(createCommand);
        ProgramUpdateCommand updateCommand = ProgramUpdateCommand.builder()
                .id(saved.asOption1().getId())
                .name(saved.asOption1().getName() + " updated")
                .description(saved.asOption1().getDescription() + " updated")
                .build();
        OneOf3<Success, NotFound, ValidationFailed> updated = underTest.send(updateCommand);

        assertThat(updated).isNotNull();
        assertThat(updated.hasOption1()).isTrue();
    }

    @Test
    public void UpdateHandler_ShouldReturnNotFound_WhenIdInvalid() {
        ProgramCreateCommand createCommand = DataUtils.buildCreateCommandI();
        OneOf2<ProgramResult, ValidationFailed> saved = underTest.send(createCommand);

        ProgramUpdateCommand updateCommand = ProgramUpdateCommand.builder()
                .id(saved.asOption1().getId() + 100L)
                .name(saved.asOption1().getName())
                .description(saved.asOption1().getDescription() + " updated")
                .build();
        OneOf3<Success, NotFound, ValidationFailed> updated = underTest.send(updateCommand);

        assertThat(updated).isNotNull();
        assertThat(updated.hasOption2()).isTrue();
    }

    @Test
    public void UpdateHandler_ShouldFail_WhenNameExists() {
        ProgramCreateCommand createCommand = DataUtils.buildCreateCommandI();
        OneOf2<ProgramResult, ValidationFailed> saved1 = underTest.send(createCommand);
        ProgramCreateCommand createCommand2 = DataUtils.buildCreateCommandII();
        OneOf2<ProgramResult, ValidationFailed> saved2 = underTest.send(createCommand2);

        //update saved1 with name of saved2
        ProgramUpdateCommand update = ProgramUpdateCommand.builder()
                .id(saved1.asOption1().getId())
                .name(saved2.asOption1().getName())
                .description(saved1.asOption1().getDescription())
                .build();

        OneOf3<Success, NotFound, ValidationFailed> result = underTest.send(update);

        assertThat(result).isNotNull();
        assertThat(result.hasOption3()).isTrue();
        assertThat(result.asOption3().getErrors()).hasSize(1);
        assertThat(result.asOption3() instanceof NameExistsValidationFailed).isTrue();

    }

    //TODO  add validation for empty name, min/max lengths

}
