package com.example.sqlstatecheck.dto;

import jakarta.validation.constraints.NotNull;

public class RequestDto {

    @NotNull
    private String fieldName;

    // Add other fields with validation annotations

    // Getters and Setters
}

public class ResponseDto {

    private String responseField;

    // Add other response fields

    // Getters and Setters
}