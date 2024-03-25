package com.play.java.bbgeducation.application.sessions.result;

import com.play.java.bbgeducation.application.common.mapping.MapFrom;
import com.play.java.bbgeducation.application.common.mapping.MapTo;
import com.play.java.bbgeducation.application.common.mapping.Mapper;
import com.play.java.bbgeducation.application.courses.results.CourseResult;
import com.play.java.bbgeducation.application.sessions.result.SessionResult;
import com.play.java.bbgeducation.domain.courses.CourseEntity;
import com.play.java.bbgeducation.domain.programs.SessionEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class SessionResultMapper implements MapTo<SessionEntity, SessionResult> {
    private final ModelMapper modelMapper;

    public SessionResultMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public SessionResult mapTo(SessionEntity sessionEntity) {
        return modelMapper.map(sessionEntity, SessionResult.class);
    }

}
