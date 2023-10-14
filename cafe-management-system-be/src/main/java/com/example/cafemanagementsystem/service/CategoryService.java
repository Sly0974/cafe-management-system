package com.example.cafemanagementsystem.service;

import com.example.cafemanagementsystem.model.dto.CategoryDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {

    ResponseEntity<String> create(CategoryDto categoryDto);
    ResponseEntity<List<CategoryDto>> findAll();
    ResponseEntity<String> update(CategoryDto categoryDto);
}
