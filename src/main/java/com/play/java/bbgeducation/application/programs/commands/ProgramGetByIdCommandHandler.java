package com.play.java.bbgeducation.application.programs.commands;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.programs.ProgramResult;
import com.play.java.bbgeducation.domain.ProgramEntity;
import com.play.java.bbgeducation.infrastructure.repositories.ProgramRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProgramGetByIdCommandHandler
        implements Command.Handler<ProgramGetByIdCommand, Optional<ProgramResult>> {
    private final ProgramRepository programRepository;

    public ProgramGetByIdCommandHandler(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    @Override
    public Optional<ProgramResult> handle(ProgramGetByIdCommand command) {

        Optional<ProgramEntity> found = programRepository.findById(command.getId());

        return found.map(programEntity -> ProgramResult.builder()
                .id(programEntity.getId())
                .name(programEntity.getName())
                .description(programEntity.getDescription())
                .build());

    }
}
