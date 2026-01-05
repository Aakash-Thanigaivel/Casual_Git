package com.example.sampledatarow.repository;

import com.example.sampledatarow.entity.SampleDataRowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleDataRowRepository extends JpaRepository<SampleDataRowEntity, Long> {
    // Define custom query methods if needed
}