package com.play.java.bbgeducation.unit.programs.commands;

import com.play.java.bbgeducation.application.common.exceptions.validation.NameExistsValidationFailed;
import com.play.java.bbgeducation.application.common.exceptions.validation.ValidationFailed;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.application.programs.commands.ProgramUpdateCommand;
import com.play.java.bbgeducation.application.programs.commands.ProgramUpdateCommandHandler;
import com.play.java.bbgeducation.domain.programs.ProgramEntity;
import com.play.java.bbgeducation.infrastructure.repositories.ProgramRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ProgramUpdateCommandHandlerTests {

    private ProgramUpdateCommandHandler underTest;
    private final ProgramRepository programRepository = Mockito.mock(ProgramRepository.class);

    @BeforeEach
    public void init(){
        underTest = new ProgramUpdateCommandHandler(programRepository);
    }

    @Test
    public void handle_ShouldReturnProgramResult_WhenInputValid(){
        ProgramUpdateCommand cmd = buildCommand();
        ProgramEntity found = buildEntityFromCommand(cmd);

        when(programRepository.findById(any(Long.class))).thenReturn(Optional.of(found));
        when(programRepository.save(any(ProgramEntity.class))).then(returnsFirstArg());

        OneOf3<Success, NotFound, ValidationFailed> result = underTest.handle(cmd);

        assertThat(result).isNotNull();
        assertThat(result.hasOption1()).isTrue();
    }

    @Test
    public void handle_ShouldReturnNotFound_WhenIdNotExists(){
        ProgramUpdateCommand cmd = buildCommand();
        when(programRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        OneOf3<Success, NotFound, ValidationFailed> result = underTest.handle(cmd);

        assertThat(result).isNotNull();
        assertThat(result.hasOption2()).isTrue();
    }

    @Test
    public void handle_ShouldReturnFail_WhenNameExists(){
        ProgramUpdateCommand cmd = buildCommand();
        ProgramEntity found = buildEntityFromCommand(cmd);
        found.setName(found.getName() + " updated");

        when(programRepository.findById(any(Long.class))).thenReturn(Optional.of(found));
        when(programRepository.findByName(cmd.getName())).thenReturn(Optional.of(found));

        OneOf3<Success, NotFound, ValidationFailed> result = underTest.handle(cmd);

        assertThat(result).isNotNull();
        assertThat(result.hasOption3()).isTrue();
        assertThat(result.asOption3().getErrors()).hasSize(1);
        assertThat(result.asOption3() instanceof NameExistsValidationFailed).isTrue();
    }

    private static ProgramUpdateCommand buildCommand() {
        return ProgramUpdateCommand.builder()
                .id(1L)
                .name("Program1")
                .description("I am program 1")
                .build();
    }

    private static ProgramEntity buildEntityFromCommand(ProgramUpdateCommand command) {
        return ProgramEntity.builder()
                .id(command.getId())
                .name(command.getName())
                .description(command.getDescription())
                .build();
    }
}
