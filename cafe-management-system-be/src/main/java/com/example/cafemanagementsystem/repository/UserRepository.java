package com.example.cafemanagementsystem.repository;

import com.example.cafemanagementsystem.model.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByEmail(String email);

    Integer updateStatus(@Param("status") String status, @Param("id") Integer id);

    List<UserEntity> findAllAdmin();
}
