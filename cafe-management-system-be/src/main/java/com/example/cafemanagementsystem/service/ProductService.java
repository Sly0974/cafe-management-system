package com.example.cafemanagementsystem.service;

import com.example.cafemanagementsystem.model.dto.ProductDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    ResponseEntity<String> create(ProductDto productDto);

    ResponseEntity<List<ProductDto>> findAll();

    ResponseEntity<String> update(ProductDto productDto);
}
