package com.play.java.bbgeducation.application.programs.result;

import com.play.java.bbgeducation.application.common.mapping.MapTo;
import com.play.java.bbgeducation.application.common.mapping.Mapper;
import com.play.java.bbgeducation.domain.programs.ProgramEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class ProgramResultListMapper
        implements MapTo<Iterable<ProgramEntity>, List<ProgramResult>> {

    private final Mapper<ProgramEntity, ProgramResult> resultMapper;

    public ProgramResultListMapper(Mapper<ProgramEntity, ProgramResult> resultMapper) {
        this.resultMapper = resultMapper;
    }

    @Override
    public List<ProgramResult> mapTo(Iterable<ProgramEntity> programEntities) {
        return  StreamSupport.stream(programEntities.spliterator(),false)
                .map(resultMapper::mapTo)
                .toList();
    }


}
