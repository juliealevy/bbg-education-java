package com.play.java.bbgeducation.application.courses.update;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.common.commands.EntityCommand;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CourseUpdateCommand extends EntityCommand
        implements Command<OneOf3<Success, NotFound, ValidationFailed>> {

    private Long id;
    private boolean isPublic;
    private boolean isOnline;

    @Builder
    public CourseUpdateCommand(Long id, String name, String description, boolean isPublic, boolean isOnline){
        super(name,description);
        this.id = id;
        this.isPublic = isPublic;
        this.isOnline = isOnline;
    }
}
