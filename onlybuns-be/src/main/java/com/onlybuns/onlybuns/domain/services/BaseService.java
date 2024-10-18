package com.onlybuns.onlybuns.domain.services;

import org.springframework.beans.factory.annotation.Autowired;

import com.onlybuns.onlybuns.core.mappers.GenericMapper;

public class BaseService {
    @Autowired
    private GenericMapper mapper;

    public <TDto, TEntity> TDto MapToDto(TEntity entity, Class<TDto> dtoClass) {
        return mapper.map(entity, dtoClass);
    }

    public <TDto, TEntity> TEntity MapToEntity(TDto dto, Class<TEntity> entityClass) {
        return mapper.map(dto, entityClass);
    }
}
