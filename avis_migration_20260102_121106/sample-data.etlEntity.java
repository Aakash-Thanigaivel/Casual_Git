package com.example.sampledata.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "SAMPLE_DATA")
public class SampleDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Map COBOL data items to fields
    // @Column(name = "COLUMN_NAME")
    // private DataType fieldName;

    // Getters and setters
}