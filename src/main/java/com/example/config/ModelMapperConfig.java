package com.example.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();

        /**
         * 전략 종류
         * STANDARD : 지능적으로 매핑한다.
         * STRICT : 정확하게 일치하는 필드만 매핑한다.
         * LOOSE : 느슨하게 매핑한다.
         */
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // 필드가 null 인 경우 매핑하지 않는다.
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        return modelMapper;
    }
}
