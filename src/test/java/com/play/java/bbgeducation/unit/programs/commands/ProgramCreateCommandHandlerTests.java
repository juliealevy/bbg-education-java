package com.play.java.bbgeducation.unit.programs.commands;

import com.play.java.bbgeducation.application.common.exceptions.validation.NameExistsValidationFailed;
import com.play.java.bbgeducation.application.common.exceptions.validation.ValidationFailed;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.programs.ProgramResult;
import com.play.java.bbgeducation.application.programs.commands.ProgramCreateCommand;
import com.play.java.bbgeducation.application.programs.commands.ProgramCreateCommandHandler;
import com.play.java.bbgeducation.domain.programs.ProgramEntity;
import com.play.java.bbgeducation.infrastructure.repositories.ProgramRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ProgramCreateCommandHandlerTests {

    private ProgramCreateCommandHandler underTest;
    private final ProgramRepository programRepository = Mockito.mock(ProgramRepository.class);

    @BeforeEach
    public void init(){
        underTest = new ProgramCreateCommandHandler(programRepository);
    }

    @Test
    public void handle_ShouldReturnProgramResult_WhenInputValid(){
        ProgramCreateCommand cmd = buildCommand();

        when(programRepository.existsByName(any(String.class))).thenReturn(false);
        when(programRepository.save(any(ProgramEntity.class))).then(returnsFirstArg());

        OneOf2<ProgramResult, ValidationFailed> result = underTest.handle(cmd);

        assertThat(result).isNotNull();
        assertThat(result.hasOption1()).isTrue();
        assertThat(result.asOption1().getId()).isNull();  //noDB involved
        assertThat(result.asOption1().getName()).isEqualTo(cmd.getName());
        assertThat(result.asOption1().getDescription()).isEqualTo(cmd.getDescription());
    }

    @Test
    public void handle_ShouldFail_WhenNameExists(){
        ProgramCreateCommand cmd = buildCommand();

        when(programRepository.existsByName(any(String.class))).thenReturn(true);

        OneOf2<ProgramResult, ValidationFailed> result = underTest.handle(cmd);

        assertThat(result).isNotNull();
        assertThat(result.hasOption2()).isTrue();
        assertThat(result.asOption2().getErrors()).hasSize(1);
        assertThat(result.asOption2() instanceof NameExistsValidationFailed).isTrue();
    }

    private static ProgramCreateCommand buildCommand() {
        return ProgramCreateCommand.builder()
                .name("Program1")
                .description("I am program 1")
                .build();
    }
}
