package com.play.java.bbgeducation.application.common.mapping;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper(){

        ModelMapper mapper =  new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE );   //for nested entities
        return mapper;
    }
}