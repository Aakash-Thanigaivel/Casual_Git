package com.example.customer.dto;

import jakarta.validation.constraints.NotNull;

public class CustomerRequest {

    @NotNull
    private Long customerId;

    // Getters and setters

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}

public class CustomerResponse {

    private Long customerId;
    private String customerName;

    public CustomerResponse(Long customerId, String customerName) {
        this.customerId = customerId;
        this.customerName = customerName;
    }

    // Getters and setters

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}