package com.play.java.bbgeducation.application.programs.update;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.common.commands.EntityCommand;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProgramUpdateCommand extends EntityCommand
        implements Command<OneOf3<Success, NotFound, ValidationFailed>> {

    private Long id;

    @Builder
    public ProgramUpdateCommand(Long id, String name, String description){
        super(name,description);
        this.id = id;
    }
}
