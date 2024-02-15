package com.play.java.bbgeducation.application.sessions.create;

import com.play.java.bbgeducation.application.common.mapping.Mapper;
import com.play.java.bbgeducation.application.sessions.result.SessionResult;
import com.play.java.bbgeducation.domain.programs.SessionEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class SessionResultMapper implements Mapper<SessionEntity, SessionResult> {
    private final ModelMapper modelMapper;

    public SessionResultMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public SessionResult mapTo(SessionEntity sessionEntity) {
        return modelMapper.map(sessionEntity, SessionResult.class);
    }

    @Override
    public SessionEntity mapFrom(SessionResult sessionResult) {
        return modelMapper.map(sessionResult, SessionEntity.class);
    }
}
