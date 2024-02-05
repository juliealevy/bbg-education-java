package com.play.java.bbgeducation.unit.programs.commands;

import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.application.programs.commands.ProgramDeleteByIdCommand;
import com.play.java.bbgeducation.application.programs.commands.ProgramDeleteByIdCommandHandler;
import com.play.java.bbgeducation.domain.programs.ProgramEntity;
import com.play.java.bbgeducation.infrastructure.repositories.ProgramRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class ProgramDeleteByIdCommandHandlerTests {
    private final ProgramRepository programRepository = Mockito.mock(ProgramRepository.class);
    private ProgramDeleteByIdCommandHandler underTest;

    @BeforeEach
    public void init(){
        underTest = new ProgramDeleteByIdCommandHandler(programRepository);
    }

    @Test
    public void handle_ShouldReturnSuccess_WhenIdExists() {
        ProgramDeleteByIdCommand cmd = ProgramDeleteByIdCommand.builder()
                .id(1L)
                .build();

        ProgramEntity found = ProgramEntity.builder()
                .id(cmd.getId())
                .build();

        when(programRepository.findById(any(Long.class))).thenReturn(Optional.of(found));
        doNothing().when(programRepository).deleteById(any(Long.class));

        OneOf2<Success, NotFound> result = underTest.handle(cmd);

        assertThat(result).isNotNull();
        assertThat(result.hasOption1()).isTrue();

    }

    @Test
    public void handle_ShouldReturnNotFound_WhenIdNotExists() {
        ProgramDeleteByIdCommand cmd = ProgramDeleteByIdCommand.builder()
                .id(1L)
                .build();
        when(programRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        OneOf2<Success, NotFound> result = underTest.handle(cmd);

        assertThat(result).isNotNull();
        assertThat(result.hasOption2()).isTrue();
    }
}
