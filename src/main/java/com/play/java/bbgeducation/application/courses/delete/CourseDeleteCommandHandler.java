package com.play.java.bbgeducation.application.courses.delete;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.domain.courses.CourseEntity;
import com.play.java.bbgeducation.infrastructure.repositories.CourseRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CourseDeleteCommandHandler implements Command.Handler<CourseDeleteCommand, OneOf2<Success,NotFound>> {

    private final CourseRepository courseRepository;

    public CourseDeleteCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public OneOf2<Success, NotFound> handle(CourseDeleteCommand command) {
        Optional<CourseEntity> found = courseRepository.findById(command.getCourseId());
        if (found.isEmpty()){
            return OneOf2.fromOption2(new NotFound());
        }

        //TODO:  if courses referenced by any sessions, then inactivate instead of delete
        //courseRepository.inactivateCourse(command.getCourseId());

        courseRepository.deleteById(command.getCourseId());
        return OneOf2.fromOption1(new Success());
    }
}
