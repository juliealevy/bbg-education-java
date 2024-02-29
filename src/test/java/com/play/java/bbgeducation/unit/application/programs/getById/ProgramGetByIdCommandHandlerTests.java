package com.play.java.bbgeducation.unit.application.programs.getById;

import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.programs.result.ProgramResult;
import com.play.java.bbgeducation.application.programs.getById.ProgramGetByIdCommand;
import com.play.java.bbgeducation.application.programs.getById.ProgramGetByIdCommandHandler;
import com.play.java.bbgeducation.domain.programs.ProgramEntity;
import com.play.java.bbgeducation.infrastructure.repositories.ProgramRepository;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ProgramGetByIdCommandHandlerTests {
    private ProgramGetByIdCommandHandler underTest;
    private ProgramRepository programRepository = Mockito.mock(ProgramRepository.class);

    @BeforeEach
    public void init(){
        underTest = new ProgramGetByIdCommandHandler(programRepository);
    }

    @Test
    public void handle_ShouldReturnResult_WhenIdExists() {

        Pair<ProgramGetByIdCommand, ProgramEntity> programObjects = buildObjects();

        ProgramEntity program = programObjects.getRight();

        when(programRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(program));

        OneOf2<ProgramResult, NotFound> result = underTest.handle(programObjects.getLeft());

        assertThat(result).isNotNull();
        assertThat(result.hasOption1()).isTrue();
        assertThat(result.asOption1()).isEqualTo(ProgramResult.builder()
                .id(program.getId())
                .name(program.getName())
                .description(program.getDescription())
                .build());

    }

    @Test
    public void handle_ShouldReturnNotFound_WhenIdNotExists(){
        Pair<ProgramGetByIdCommand, ProgramEntity> programObjects = buildObjects();

        when(programRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());

        OneOf2<ProgramResult, NotFound> result = underTest.handle(programObjects.getLeft());

        assertThat(result).isNotNull();
        assertThat(result.hasOption2()).isTrue();
    }

    private Pair<ProgramGetByIdCommand, ProgramEntity> buildObjects() {
        ProgramGetByIdCommand cmd = ProgramGetByIdCommand.builder()
                .id(1L)
                .build();

        ProgramEntity found = ProgramEntity.build(
                cmd.getId(),
                "Program 1",
                "This is program 1");

        return Pair.of(cmd, found);
    }
}
