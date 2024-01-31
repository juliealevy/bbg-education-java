package com.play.java.bbgeducation.application.programs.commands;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.exceptions.NameExistsException;
import com.play.java.bbgeducation.application.programs.ProgramResult;
import com.play.java.bbgeducation.domain.ProgramEntity;
import com.play.java.bbgeducation.infrastructure.repositories.ProgramRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProgramUpdateCommandHandler implements Command.Handler<ProgramUpdateCommand, Optional<ProgramResult>>{
    private final ProgramRepository programRepository;

    public ProgramUpdateCommandHandler(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    @SneakyThrows
    @Override
    public Optional<ProgramResult> handle(ProgramUpdateCommand command) {

        Optional<ProgramEntity> found = programRepository.findById(command.getId());
        if (found.isEmpty()){
            return Optional.empty();
        }

        if (!found.get().getName().equals(command.getName())){
            Optional<ProgramEntity> match = programRepository.findByName(command.getName());
            if (match.isPresent()){
                throw new NameExistsException("Program");
            }
        }
        ProgramEntity entityToSave = ProgramEntity.builder()
                        .id(command.getId())
                        .name(command.getName())
                        .description(command.getDescription())
                        .build();

        ProgramEntity saved = programRepository.save(entityToSave);
        return Optional.of(ProgramResult.builder()
                .id(saved.getId())
                .name(saved.getName())
                .description(saved.getDescription())
                .build()
        );
    }
}
