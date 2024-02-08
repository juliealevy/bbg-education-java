package com.play.java.bbgeducation.unit.programs.commands.getAll;

import an.awesome.pipelinr.repack.com.google.common.collect.Iterables;
import com.play.java.bbgeducation.application.programs.result.ProgramResult;
import com.play.java.bbgeducation.application.programs.getAll.ProgramGetAllCommand;
import com.play.java.bbgeducation.application.programs.getAll.ProgramGetAllCommandHandler;
import com.play.java.bbgeducation.domain.programs.ProgramEntity;
import com.play.java.bbgeducation.infrastructure.repositories.ProgramRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

public class ProgramGetAllCommandHandlerTests {
    ProgramGetAllCommandHandler underTest;
    ProgramRepository programRepository = Mockito.mock(ProgramRepository.class);

    @BeforeEach
    public void init(){
        underTest = new ProgramGetAllCommandHandler(programRepository);
    }

    @Test
    public void handle_shouldReturnList_whenValid(){
        ProgramGetAllCommand cmd = ProgramGetAllCommand.builder().build();
        Iterable<ProgramEntity> entityList = Arrays.asList(
                buildProgramEntity(1L), buildProgramEntity(2L));

        when(programRepository.findAll()).thenReturn(entityList);
        List<ProgramResult> results = underTest.handle(cmd);

        assertThat(results).isNotNull();
        assertThat(results.size()).isEqualTo(Iterables.size(entityList));

    }

    @Test
    public void handle_canReturnEmptyList_whenValid(){
        ProgramGetAllCommand cmd = ProgramGetAllCommand.builder().build();
        Iterable<ProgramEntity> entityList = new ArrayList<>();

        when(programRepository.findAll()).thenReturn(entityList);
        List<ProgramResult> results = underTest.handle(cmd);

        assertThat(results).isNotNull();
        assertThat(results.size()).isEqualTo(Iterables.size(entityList));
    }

    private ProgramEntity buildProgramEntity(Long id){
        return ProgramEntity.builder()
                .id(id)
                .name("Program " + id)
                .description("I am program " + id)
                .build();
    }
}
