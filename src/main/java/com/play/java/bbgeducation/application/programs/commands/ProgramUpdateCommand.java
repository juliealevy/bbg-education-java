package com.play.java.bbgeducation.application.programs.commands;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.common.exceptions.validation.ValidationFailed;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgramUpdateCommand implements Command<OneOf3<Success, NotFound, ValidationFailed>> {
    private Long id;
    private String name;
    private String description;
}
