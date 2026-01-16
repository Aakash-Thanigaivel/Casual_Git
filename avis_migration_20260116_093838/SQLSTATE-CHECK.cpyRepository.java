package com.migration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.migration.entity.SQLSTATE-CHECKEntity;

@Repository
public interface SQLSTATE-CHECKRepository extends JpaRepository<SQLSTATE-CHECKEntity, Long> {
    // Custom query methods here
}
