package com.onlybuns.onlybuns.core.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GenericMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public GenericMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    // Generic method to map an entity to a DTO
    public <TEntity, TDto> TDto map(TEntity entity, Class<TDto> dtoClass) {
        return modelMapper.map(entity, dtoClass);
    }
}