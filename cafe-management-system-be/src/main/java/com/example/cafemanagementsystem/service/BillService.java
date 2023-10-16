package com.example.cafemanagementsystem.service;

import com.example.cafemanagementsystem.model.dto.BillDto;
import org.springframework.http.ResponseEntity;

public interface BillService {
    ResponseEntity<String> generateReport(BillDto billDto);
}
