package com.example.cafemanagementsystem.mapper;

import com.example.cafemanagementsystem.model.dto.CategoryDto;
import com.example.cafemanagementsystem.model.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDto categoryEntityToCategoryDto(CategoryEntity categoryEntity);

    CategoryEntity categoryDtoToCategoryEntity(CategoryDto categoryDto);

}
