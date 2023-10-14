package com.example.cafemanagementsystem.mapper;

import com.example.cafemanagementsystem.model.dto.ProductDto;
import com.example.cafemanagementsystem.model.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDto productEntityToProductDto(ProductEntity productEntity);

    ProductEntity productDtoToProductEntity(ProductDto productDto);

}
