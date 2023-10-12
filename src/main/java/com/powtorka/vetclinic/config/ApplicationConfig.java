package com.powtorka.vetclinic.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class ApplicationConfig {

    @Bean
    public ModelMapper modelMapper(Set<Converter> converters) {
        ModelMapper modelMapper = new ModelMapper();
        converters.forEach(modelMapper::addConverter);
        return modelMapper;
    }
}
