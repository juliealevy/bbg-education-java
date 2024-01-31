package com.play.java.bbgeducation.application.mapping.impl;

import com.play.java.bbgeducation.api.programs.ProgramRequest;
import com.play.java.bbgeducation.application.mapping.MapFrom;
import com.play.java.bbgeducation.application.mapping.MapTo;
import com.play.java.bbgeducation.application.mapping.Mapper;
import com.play.java.bbgeducation.domain.ProgramEntity;
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
