package com.example.cafemanagementsystem.repository;

import com.example.cafemanagementsystem.model.entity.BillEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillRepository extends JpaRepository<BillEntity, Integer> {

    List<BillEntity> findByCreatedBy(String createdBy);
}
