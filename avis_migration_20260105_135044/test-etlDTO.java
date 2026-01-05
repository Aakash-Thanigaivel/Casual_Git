package com.example.testetl.dto;

import jakarta.validation.constraints.NotNull;

public class CustomerRequest {

    @NotNull
    private String name;

    @NotNull
    private String address;

    // Other fields and getters/setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

public class CustomerResponse {

    private String status;

    // Other fields and getters/setters

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}