package com.play.java.bbgeducation.application.programs.commands;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.oneof.OneOf2;
import com.play.java.bbgeducation.application.exceptions.ValidationFailed;
import com.play.java.bbgeducation.application.programs.ProgramResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgramCreateCommand implements Command<OneOf2<ProgramResult, ValidationFailed>> {
    private String name;
    private String description;

}
//public class ProgramCreateCommand implements Request<ProgramResult> {
//    private String name;
//    private String description;
//
//}

