package com.example.sampledata.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sample_data")
public class SampleDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "filler1")
    private String filler1;

    @Column(name = "filler2")
    private String filler2;

    @Column(name = "filler3")
    private String filler3;

    // Getters and setters
}