package com.play.java.bbgeducation.application.programs.commands;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.programs.ProgramResult;
import com.play.java.bbgeducation.domain.ProgramEntity;
import com.play.java.bbgeducation.infrastructure.repositories.ProgramRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class ProgramGetAllCommandHandler
        implements Command.Handler<ProgramGetAllCommand, List<ProgramResult>>{

    private final ProgramRepository programRepository;

    public ProgramGetAllCommandHandler(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    @Override
    public List<ProgramResult> handle(ProgramGetAllCommand programGetAllCommand) {
        Iterable<ProgramEntity> programs = programRepository.findAll();

        return StreamSupport.stream(programs.spliterator(), false)
                .map(p -> ProgramResult.builder()
                        .id(p.getId())
                        .name(p.getName())
                        .description(p.getDescription())
                        .build()).collect(Collectors.toList());

    }
}
