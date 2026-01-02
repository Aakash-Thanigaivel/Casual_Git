package com.example.testetl.dto;

import jakarta.validation.constraints.NotNull;

public class CustomerRequest {

    @NotNull
    private String name;

    // Other fields

    // Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Other getters and setters
}

public class CustomerResponse {

    private String status;

    // Other fields

    // Getters and setters

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Other getters and setters
}