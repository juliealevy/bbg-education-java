package com.play.java.bbgeducation.application.courses.create;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.courses.results.CourseResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseCreateCommand implements Command<OneOf2<CourseResult, ValidationFailed>> {
    private String name;
    private String description;
    private boolean isPublic;
    private boolean isOnline;
}
