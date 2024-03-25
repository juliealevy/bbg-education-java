package com.play.java.bbgeducation.application.sessions.addCourse;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.common.commands.EntityCommand;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddCourseCommand extends EntityCommand implements Command<OneOf2<Success, NotFound>> {
    private Long programId;
    private Long sessionId;
    private Long courseId;

    public AddCourseCommand(Long programId, Long sesionId, Long courseId){
        this.programId = programId;
        this.sessionId = sesionId;
        this.courseId = courseId;
    }
}
