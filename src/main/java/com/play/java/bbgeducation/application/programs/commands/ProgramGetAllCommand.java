package com.play.java.bbgeducation.application.programs.commands;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.programs.ProgramResult;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProgramGetAllCommand implements Command<List<ProgramResult>> {

    /****
     for now leaving return as just the list, not a OneOf.   If there is a need for common
     handling for these responses in the pipeline, then i will switch to OneOf2<List<ProgramResult>, Voidy>>
     or something like that
     *****/
}
