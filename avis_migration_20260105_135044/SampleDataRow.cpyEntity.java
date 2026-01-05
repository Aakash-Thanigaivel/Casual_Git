package com.example.sampledatarow.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "SAMPLE_DATA_ROW")
public class SampleDataRowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "FILLER1")
    private String filler1;

    @Column(name = "FILLER2")
    private String filler2;

    @Column(name = "FILLER3")
    private String filler3;

    // Getters and Setters
}