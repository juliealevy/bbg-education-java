package com.play.java.bbgeducation.application.courses.update;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.application.common.validation.NameExistsValidationFailed;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.domain.courses.CourseEntity;
import com.play.java.bbgeducation.infrastructure.repositories.CourseRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CourseUpdateCommandHandler implements Command.Handler<CourseUpdateCommand, OneOf3<Success, NotFound, ValidationFailed>> {

    private final CourseRepository courseRepository;

    public CourseUpdateCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public OneOf3<Success, NotFound, ValidationFailed> handle(CourseUpdateCommand command) {

        Optional<CourseEntity> found = courseRepository.findById(command.getId());
        if (found.isEmpty()){
            return OneOf3.fromOption2(new NotFound());
        }
        if (!command.getName().equals(found.get().getName()) &&
                courseRepository.existsByName(command.getName())){
            return OneOf3.fromOption3(new NameExistsValidationFailed("course"));
        }

        CourseEntity courseToUpdate = CourseEntity.build(
                command.getId(),
                command.getName(),
                command.getDescription(),
                command.isPublic(),
                command.isOnline());

        courseRepository.save(courseToUpdate);
        return OneOf3.fromOption1(new Success());
    }
}
