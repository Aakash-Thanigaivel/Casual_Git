package com.example.testetl.dto;

import jakarta.validation.constraints.NotNull;

public class CustomerRequest {

    @NotNull
    private String name;

    @NotNull
    private String address;

    // Getters and setters
}

public class CustomerResponse {

    private String status;
    private String message;

    // Getters and setters
}