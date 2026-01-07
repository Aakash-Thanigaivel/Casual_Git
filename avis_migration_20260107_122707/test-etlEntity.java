package com.example.testetl.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "CUSTOMER_DATA")
public class CustomerData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "RECORDS_READ")
    private int recordsRead;

    @Column(name = "RECORDS_PROCESSED")
    private int recordsProcessed;

    @Column(name = "RECORDS_ERROR")
    private int recordsError;

    @Column(name = "TOTAL_BALANCE")
    private double totalBalance;

    @Column(name = "AVERAGE_BALANCE")
    private double averageBalance;

    @Column(name = "AVAILABLE_CREDIT")
    private double availableCredit;

    @Column(name = "RISK_LEVEL")
    private String riskLevel;

    @Column(name = "YEAR")
    private int year;

    @Column(name = "MONTH")
    private int month;

    @Column(name = "DAY")
    private int day;

    @Column(name = "FORMATTED_DATE")
    private String formattedDate;

    // Getters and setters
}