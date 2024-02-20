package com.play.java.bbgeducation.application.programs.create;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.common.commands.EntityCommand;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.programs.result.ProgramResult;
import lombok.Builder;
import lombok.Data;

@Data
public class ProgramCreateCommand extends EntityCommand
        implements Command<OneOf2<ProgramResult, ValidationFailed>>
{
    @Builder
    public ProgramCreateCommand(String name, String description){
        super(name, description);
    }
}


