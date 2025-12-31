package com.example.sqlstatecheck.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "SQLSTATE_CHECK")
public class SqlStateCheckEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Map COBOL data items to entity fields
    // Example:
    // @Column(name = "FIELD_NAME")
    // private String fieldName;

    // Getters and setters
}