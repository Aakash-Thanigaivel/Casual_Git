package com.example.sampledata.repository;

import com.example.sampledata.entity.SampleDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleDataRepository extends JpaRepository<SampleDataEntity, Long> {
    // Define custom query methods if needed
}