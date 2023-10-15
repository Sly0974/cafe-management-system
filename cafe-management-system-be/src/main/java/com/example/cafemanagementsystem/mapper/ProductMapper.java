package com.example.cafemanagementsystem.mapper;

import com.example.cafemanagementsystem.model.dto.ProductDto;
import com.example.cafemanagementsystem.model.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "category.id", target= "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    ProductDto productEntityToProductDto(ProductEntity productEntity);

    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(source = "categoryName", target = "category.name")
    ProductEntity productDtoToProductEntity(ProductDto productDto);

}
