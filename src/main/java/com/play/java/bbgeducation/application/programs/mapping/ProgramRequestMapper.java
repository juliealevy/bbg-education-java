package com.play.java.bbgeducation.application.programs.mapping;

import com.play.java.bbgeducation.api.programs.ProgramRequest;
import com.play.java.bbgeducation.application.common.mapping.Mapper;
import com.play.java.bbgeducation.domain.programs.ProgramEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProgramRequestMapper implements Mapper<ProgramEntity,ProgramRequest>

{

    private final ModelMapper mapper;

    public ProgramRequestMapper(ModelMapper modelMapper) {
        this.mapper = modelMapper;
    }
    @Override
    public ProgramRequest mapTo(ProgramEntity programEntity) {
        return mapper.map(programEntity, ProgramRequest.class);
    }

    @Override
    public ProgramEntity mapFrom(ProgramRequest programRequest) {
        return mapper.map(programRequest, ProgramEntity.class);
    }
}
