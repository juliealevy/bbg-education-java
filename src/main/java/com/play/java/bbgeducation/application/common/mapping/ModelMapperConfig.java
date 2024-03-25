package com.play.java.bbgeducation.application.common.mapping;

import com.play.java.bbgeducation.application.courses.results.CourseResult;
import com.play.java.bbgeducation.application.sessions.result.SessionResult;
import com.play.java.bbgeducation.domain.courses.CourseEntity;
import com.play.java.bbgeducation.domain.programs.SessionEntity;
import com.play.java.bbgeducation.domain.sessioncourse.SessionCourseEntity;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);   //for nested entities

        Converter<Set<SessionCourseEntity>, Set<CourseResult>> convertCourses = new AbstractConverter<>() {
            @Override
            protected Set<CourseResult> convert(Set<SessionCourseEntity> source) {
                return source.stream().map(sessionCourseEntity -> {
                    return mapper.map(sessionCourseEntity.getCourse(), CourseResult.class);
                }).collect(Collectors.toSet());
            }
        };

        mapper.typeMap(SessionEntity.class, SessionResult.class)
                .addMappings(sessionMap -> sessionMap.using(convertCourses)
                        .map(SessionEntity::getSessionCourses, SessionResult::setCourses)
                );

        return mapper;
    }
}
