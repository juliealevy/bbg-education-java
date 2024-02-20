package com.play.java.bbgeducation.application.courses.create;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.common.commands.EntityCommand;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.courses.results.CourseResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CourseCreateCommand extends EntityCommand
        implements Command<OneOf2<CourseResult, ValidationFailed>> {

    private boolean isPublic;
    private boolean isOnline;

    @Builder
    public CourseCreateCommand(String name, String description, boolean isPublic, boolean isOnline){
        super(name,description);
        this.isPublic = isPublic;
        this.isOnline = isOnline;
    }
}
