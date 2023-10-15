package com.example.cafemanagementsystem.service;

import com.example.cafemanagementsystem.model.dto.ProductDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    ResponseEntity<String> create(ProductDto productDto);
    ResponseEntity<List<ProductDto>> findAll();
    ResponseEntity<ProductDto> findById(Integer id);
    ResponseEntity<String> update(ProductDto productDto);
    ResponseEntity<String> delete(Integer id);
}
