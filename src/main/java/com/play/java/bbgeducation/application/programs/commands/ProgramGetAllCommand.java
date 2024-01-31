package com.play.java.bbgeducation.application.programs.commands;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.programs.ProgramResult;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProgramGetAllCommand implements Command<List<ProgramResult>> {

}
