package com.play.java.bbgeducation.application.programs.create;

import an.awesome.pipelinr.Command;
import br.com.fluentvalidator.Validator;
import br.com.fluentvalidator.context.ValidationResult;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.exceptions.validation.NameExistsValidationFailed;
import com.play.java.bbgeducation.application.common.exceptions.validation.ValidationFailed;
import com.play.java.bbgeducation.application.programs.ProgramResult;
import com.play.java.bbgeducation.domain.programs.ProgramEntity;
import com.play.java.bbgeducation.infrastructure.repositories.ProgramRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import static com.play.java.bbgeducation.application.common.exceptions.validation.ValidationFailed.BadRequest;

@Component
public class ProgramCreateCommandHandler
        implements Command.Handler<ProgramCreateCommand, OneOf2<ProgramResult, ValidationFailed>>
{
    private final ProgramRepository programRepository;
    private final Validator<ProgramCreateCommand> createCommandValidator;

    public ProgramCreateCommandHandler(ProgramRepository programRepository, Validator<ProgramCreateCommand> createCommandValidator) {
        this.programRepository = programRepository;
        this.createCommandValidator = createCommandValidator;
    }

    @SneakyThrows
    @Override
    public OneOf2<ProgramResult, ValidationFailed> handle(ProgramCreateCommand createProgramCommand) {

        ValidationResult validationResults = createCommandValidator.validate(createProgramCommand);

        if (!validationResults.isValid()) {
            return OneOf2.fromOption2(ValidationFailed.Conflict(validationResults.getErrors()));
        }

        if (programRepository.existsByName(createProgramCommand.getName())) {
            return OneOf2.fromOption2(new NameExistsValidationFailed("program"));
        }


        ProgramEntity programEntity = ProgramEntity.builder()
                .name(createProgramCommand.getName())
                .description(createProgramCommand.getDescription())
                .build();
        ProgramEntity newProgram = programRepository.save(programEntity);

        return OneOf2.fromOption1(ProgramResult.builder()
                .id(newProgram.getId())
                .name(newProgram.getName())
                .description(newProgram.getDescription())
                .build());
    }

}
