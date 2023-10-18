package com.example.cafemanagementsystem.service;

import com.example.cafemanagementsystem.model.dto.BillDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BillService {
    ResponseEntity<String> generateReport(BillDto billDto);

    ResponseEntity<List<BillDto>> findAll();

    ResponseEntity<String> delete(Integer id);

    ResponseEntity<byte[]> findPdf(Integer id);
}
