package com.example.cafemanagementsystem.mapper;

import com.example.cafemanagementsystem.model.dto.BillDto;
import com.example.cafemanagementsystem.model.entity.BillEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BillMapper {
    BillMapper INSTANCE = Mappers.getMapper(BillMapper.class);

    BillDto billEntityToBillDto(BillEntity billEntity);

    BillEntity billDtoToBillEntity(BillDto billDto);
}
