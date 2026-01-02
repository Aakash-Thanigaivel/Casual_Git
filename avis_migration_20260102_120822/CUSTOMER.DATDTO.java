package com.example.customer.dto;

import jakarta.validation.constraints.NotNull;

public class CustomerRequest {

    @NotNull
    private String name;

    // Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

public class CustomerResponse {

    private String name;

    // Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}