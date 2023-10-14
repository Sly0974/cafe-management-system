package com.example.cafemanagementsystem.service.impl;

import com.example.cafemanagementsystem.model.dto.ProductDto;
import com.example.cafemanagementsystem.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Override
    public ResponseEntity<String> create(ProductDto productDto) {
        return null;
    }

    @Override
    public ResponseEntity<List<ProductDto>> findAll() {
        return null;
    }

    @Override
    public ResponseEntity<String> update(ProductDto productDto) {
        return null;
    }
}
