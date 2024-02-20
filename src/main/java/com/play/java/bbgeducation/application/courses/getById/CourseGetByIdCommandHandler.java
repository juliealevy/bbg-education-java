package com.play.java.bbgeducation.application.courses.getById;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.common.mapping.Mapper;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.courses.results.CourseResult;
import com.play.java.bbgeducation.domain.courses.CourseEntity;
import com.play.java.bbgeducation.infrastructure.repositories.CourseRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CourseGetByIdCommandHandler implements Command.Handler<CourseGetByIdCommand, OneOf2<CourseResult, NotFound>> {

    private final CourseRepository courseRepository;
    private final Mapper<CourseEntity, CourseResult> mapper;

    public CourseGetByIdCommandHandler(CourseRepository courseRepository, Mapper<CourseEntity, CourseResult> mapper) {
        this.courseRepository = courseRepository;
        this.mapper = mapper;
    }

    @Override
    public OneOf2<CourseResult, NotFound> handle(CourseGetByIdCommand command) {
        Optional<CourseEntity> found = courseRepository.findById(command.getCourseId());

        return found.<OneOf2<CourseResult, NotFound>>map(
                courseEntity -> OneOf2.fromOption1(mapper.mapTo(courseEntity)))
                .orElseGet(() -> OneOf2.fromOption2(new NotFound()));
    }
}
