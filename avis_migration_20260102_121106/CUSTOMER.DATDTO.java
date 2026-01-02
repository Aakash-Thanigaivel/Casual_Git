package com.example.customer.dto;

import jakarta.validation.constraints.NotNull;

public class CustomerRequest {

    @NotNull
    private Long customerId;

    // Other fields and validation annotations

    // Getters and setters
}

public class CustomerResponse {

    // Fields for response

    // Getters and setters
}