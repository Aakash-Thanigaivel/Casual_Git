package com.migration.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "SAMPLEDATAROW")
public class SampleDataRowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: Add COBOL data item mappings

    // Getters and setters
}
