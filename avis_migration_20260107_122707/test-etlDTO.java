package com.example.testetl.dto;

import jakarta.validation.constraints.NotNull;

public class CustomerDataRequest {

    @NotNull
    private int recordsRead;

    @NotNull
    private int recordsProcessed;

    @NotNull
    private int recordsError;

    @NotNull
    private double totalBalance;

    @NotNull
    private double averageBalance;

    @NotNull
    private double availableCredit;

    @NotNull
    private String riskLevel;

    @NotNull
    private int year;

    @NotNull
    private int month;

    @NotNull
    private int day;

    @NotNull
    private String formattedDate;

    // Getters and setters
}

public class CustomerDataResponse {
    // Response fields and getters/setters
}