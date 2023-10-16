package com.example.cafemanagementsystem.service.impl;

import com.example.cafemanagementsystem.config.security.JwtFilter;
import com.example.cafemanagementsystem.constants.CafeConstants;
import com.example.cafemanagementsystem.mapper.BillMapper;
import com.example.cafemanagementsystem.model.dto.BillDto;
import com.example.cafemanagementsystem.model.entity.BillEntity;
import com.example.cafemanagementsystem.repository.BillRepository;
import com.example.cafemanagementsystem.service.BillService;
import com.example.cafemanagementsystem.util.CafeUtils;
import com.example.cafemanagementsystem.util.PdfUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;

    private final JwtFilter jwtFilter;

    public BillServiceImpl(BillRepository billRepository, JwtFilter jwtFilter) {
        this.billRepository = billRepository;
        this.jwtFilter = jwtFilter;
    }

    @Override
    public ResponseEntity<String> generateReport(BillDto billDto) {
        try {
            billDto.setUuid(CafeUtils.getUUID());
            billDto.setCreatedBy(jwtFilter.getCurrentUser());
            BillEntity billEntity = BillMapper.INSTANCE.billDtoToBillEntity(billDto);
            billRepository.save(billEntity);
            PdfUtils.generateAndSaveBillReport(billDto);
            return CafeUtils.getResponseEntity(String.format("Successfully create bill - [uuid:%s]", billEntity.getUuid()), HttpStatus.CREATED);
        } catch (Exception ex) {
            log.error("Failed call generateReport", ex);
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
