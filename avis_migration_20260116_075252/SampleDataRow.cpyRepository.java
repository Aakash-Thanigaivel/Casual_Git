package com.migration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.migration.entity.SampleDataRowEntity;

@Repository
public interface SampleDataRowRepository extends JpaRepository<SampleDataRowEntity, Long> {
    // Custom query methods here
}
