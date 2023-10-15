package com.example.cafemanagementsystem.controller;

import com.example.cafemanagementsystem.constants.CafeConstants;
import com.example.cafemanagementsystem.model.dto.CategoryDto;
import com.example.cafemanagementsystem.model.dto.ProductDto;
import com.example.cafemanagementsystem.service.ProductService;
import com.example.cafemanagementsystem.util.CafeUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    ResponseEntity<String> create(@NotNull @Valid @RequestBody ProductDto productDto) {
        try {
            return productService.create(productDto);
        } catch (Exception ex) {
            log.error("Failed call create", ex);
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    ResponseEntity<List<ProductDto>> findAll() {
        try {
            return productService.findAll();
        } catch (Exception ex) {
            log.error("Failed call findAll", ex);
            return new ResponseEntity<>(new ArrayList<ProductDto>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/{id}")
    ResponseEntity<ProductDto> findById(@PathVariable Integer id) {
        try {
            return productService.findById(id);
        } catch (Exception ex) {
            log.error("Failed call delete", ex);
            return new ResponseEntity<>(new ProductDto(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    ResponseEntity<String> update(@RequestBody ProductDto productDto) {
        try {
            return productService.update(productDto);
        } catch (Exception ex) {
            log.error("Failed update", ex);
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    ResponseEntity<String> delete(@PathVariable Integer id) {
        try {
            return productService.delete(id);
        } catch (Exception ex) {
            log.error("Failed call delete", ex);
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
