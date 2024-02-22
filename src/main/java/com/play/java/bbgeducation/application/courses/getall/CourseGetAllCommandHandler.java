package com.play.java.bbgeducation.application.courses.getall;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.StreamSupplier;
import com.play.java.bbgeducation.application.common.mapping.Mapper;
import com.play.java.bbgeducation.application.courses.results.CourseResult;
import com.play.java.bbgeducation.domain.courses.CourseEntity;
import com.play.java.bbgeducation.infrastructure.repositories.CourseRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class CourseGetAllCommandHandler implements Command.Handler<CourseGetAllCommand, List<CourseResult>>{

    private final CourseRepository courseRepository;
    private final Mapper<CourseEntity, CourseResult> mapper;

    public CourseGetAllCommandHandler(CourseRepository courseRepository, Mapper<CourseEntity, CourseResult> mapper) {
        this.courseRepository = courseRepository;
        this.mapper = mapper;
    }

    @Override
    public List<CourseResult> handle(CourseGetAllCommand command) {
        return StreamSupport.stream(courseRepository.findAll().spliterator(), false)
                .map(mapper::mapTo)
                .toList();

    }
}
