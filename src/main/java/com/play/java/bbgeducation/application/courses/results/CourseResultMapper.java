package com.play.java.bbgeducation.application.courses.results;

import com.play.java.bbgeducation.application.common.mapping.Mapper;
import com.play.java.bbgeducation.domain.courses.CourseEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CourseResultMapper implements Mapper<CourseEntity, CourseResult> {

    private final ModelMapper modelMapper;

    public CourseResultMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public CourseResult mapTo(CourseEntity courseEntity) {
        return modelMapper.map(courseEntity, CourseResult.class);
    }

    @Override
    public CourseEntity mapFrom(CourseResult courseResult) {
        return modelMapper.map(courseResult, CourseEntity.class);
    }
}
