package com.play.java.bbgeducation.integration.programs.commandtests;

import an.awesome.pipelinr.Pipeline;
import com.play.java.bbgeducation.application.OneOf2;
import com.play.java.bbgeducation.application.exceptions.NameExistsException;
import com.play.java.bbgeducation.application.exceptions.ValidationFailed;
import com.play.java.bbgeducation.application.programs.ProgramResult;
import com.play.java.bbgeducation.application.programs.commands.ProgramCreateCommand;
import com.play.java.bbgeducation.application.programs.commands.ProgramUpdateCommand;
import com.play.java.bbgeducation.integration.programs.DataUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProgramUpdateCommandHandlerTests {
    private final Pipeline underTest;

    @Autowired
    public ProgramUpdateCommandHandlerTests(Pipeline pipeline){
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
        Optional<ProgramResult> updated = underTest.send(updateCommand);

        assertThat(updated).isPresent();
        assertThat(updated.get().getId()).isEqualTo(saved.asOption1().getId());
        assertThat(updated.get().getName()).isEqualTo(updateCommand.getName());
        assertThat(updated.get().getDescription()).isEqualTo(updateCommand.getDescription());
    }

    @Test
    public void UpdateHandler_ShouldFail_WhenNameExists() {
        ProgramCreateCommand createCommand = DataUtils.buildCreateCommandI();
        OneOf2<ProgramResult, ValidationFailed> saved1 = underTest.send(createCommand);
        ProgramCreateCommand createCommand2 = DataUtils.buildCreateCommandII();
        OneOf2<ProgramResult, ValidationFailed> saved2 = underTest.send(createCommand2);

        //update saved1 with name of saved2
        ProgramUpdateCommand updated = ProgramUpdateCommand.builder()
                .id(saved1.asOption1().getId())
                .name(saved2.asOption1().getName())
                .description(saved1.asOption1().getDescription())
                .build();

        assertThatThrownBy(() -> underTest.send(updated))
                .isInstanceOf(NameExistsException.class);
    }

    //TODO  add validation for empty name, min/max lengths

}
