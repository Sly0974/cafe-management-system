package com.example.cafemanagementsystem.service.impl;

import com.example.cafemanagementsystem.repository.BillRepository;
import com.example.cafemanagementsystem.repository.CategoryRepository;
import com.example.cafemanagementsystem.repository.ProductRepository;
import com.example.cafemanagementsystem.service.DashboardService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardServiceImp implements DashboardService {

    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    private final BillRepository billRepository;

    @Autowired
    public DashboardServiceImp(@NotNull final CategoryRepository categoryRepository,
                               @NotNull final ProductRepository productRepository,
                               @NotNull final BillRepository billRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.billRepository = billRepository;
    }

    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        final Map<String, Object> map = new HashMap<>();
        map.put("category", categoryRepository.count());
        map.put("product", productRepository.count());
        map.put("bill", billRepository.count());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
