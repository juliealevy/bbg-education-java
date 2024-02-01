package com.play.java.bbgeducation.application.programs.commands;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.exceptions.ValidationFailed;
import com.play.java.bbgeducation.application.oneof.OneOf3;
import com.play.java.bbgeducation.application.oneof.OneOfTypes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgramUpdateCommand implements Command<OneOf3<OneOfTypes.Success, OneOfTypes.NotFound, ValidationFailed>> {
    private Long id;
    private String name;
    private String description;
}
