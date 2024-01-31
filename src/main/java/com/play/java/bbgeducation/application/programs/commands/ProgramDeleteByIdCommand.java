package com.play.java.bbgeducation.application.programs.commands;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.programs.ProgramResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgramDeleteByIdCommand implements Command<Optional<ProgramResult>> {
    private Long id;

    //temporarily return programResult optional until get validation objects returning commandhandlers
}
