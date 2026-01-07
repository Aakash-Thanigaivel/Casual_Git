package com.example.testetl.repository;

import com.example.testetl.entity.CustomerData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerDataRepository extends JpaRepository<CustomerData, Long> {
    // Custom query methods if needed
}