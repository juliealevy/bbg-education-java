package com.play.java.bbgeducation.application.programs.result;

import com.play.java.bbgeducation.application.common.mapping.Mapper;
import com.play.java.bbgeducation.domain.programs.ProgramEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProgramResultMapper implements Mapper<ProgramEntity, ProgramResult> {

    private final ModelMapper modelMapper;

    public ProgramResultMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ProgramResult mapTo(ProgramEntity programEntity) {
        return modelMapper.map(programEntity, ProgramResult.class);
    }

    @Override
    public ProgramEntity mapFrom(ProgramResult programResult) {
        return modelMapper.map(programResult, ProgramEntity.class);
    }
}
