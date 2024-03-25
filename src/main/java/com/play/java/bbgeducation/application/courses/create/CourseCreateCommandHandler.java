package com.play.java.bbgeducation.application.courses.create;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.common.mapping.Mapper;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.validation.NameExistsValidationFailed;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.courses.results.CourseResult;
import com.play.java.bbgeducation.domain.courses.CourseEntity;
import com.play.java.bbgeducation.infrastructure.repositories.CourseRepository;
import org.springframework.stereotype.Component;

@Component
public class CourseCreateCommandHandler implements Command.Handler<CourseCreateCommand, OneOf2<CourseResult, ValidationFailed>>
{
    private final CourseRepository courseRepository;
    private final Mapper<CourseEntity, CourseResult> mapper;

    public CourseCreateCommandHandler(CourseRepository courseRepository, Mapper<CourseEntity, CourseResult> mapper) {
        this.courseRepository = courseRepository;
        this.mapper = mapper;
    }

    @Override
    public OneOf2<CourseResult, ValidationFailed> handle(CourseCreateCommand command) {
        if (courseRepository.existsByName(command.getName())){
            return OneOf2.fromOption2(new NameExistsValidationFailed("course"));
        }

        CourseEntity courseToCreate = CourseEntity.create(
                command.getName(),
                command.getDescription(),
                command.isPublic(),
                command.isOnline());


        CourseEntity saved = courseRepository.save(courseToCreate);
        return OneOf2.fromOption1(mapper.mapTo(saved));
    }
}
