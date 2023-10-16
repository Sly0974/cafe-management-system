package com.example.cafemanagementsystem.repository;

import com.example.cafemanagementsystem.model.entity.BillEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<BillEntity, Integer> {
}
