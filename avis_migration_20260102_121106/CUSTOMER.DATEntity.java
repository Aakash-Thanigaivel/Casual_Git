package com.example.customer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "CUSTOMER")
public class Customer {

    @Id
    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    // Map other COBOL data items to fields
    // Example:
    // @Column(name = "NAME")
    // private String name;

    // Getters and setters
}