package com.example.cafemanagementsystem.mapper;

import com.example.cafemanagementsystem.model.dto.UserDto;
import com.example.cafemanagementsystem.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto userEntityToUserDto(UserEntity userEntity);
}
