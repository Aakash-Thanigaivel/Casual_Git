package com.example.sqlstatecheck.repository;

import com.example.sqlstatecheck.entity.SqlStateCheckEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SqlStateCheckRepository extends JpaRepository<SqlStateCheckEntity, Long> {
    // Define custom query methods if needed
}