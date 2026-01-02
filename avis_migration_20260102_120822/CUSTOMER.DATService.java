package com.example.customer.service;

import com.example.customer.dto.CustomerRequest;
import com.example.customer.dto.CustomerResponse;
import com.example.customer.entity.Customer;
import com.example.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public CustomerResponse processCustomer(CustomerRequest request) {
        // Business logic equivalent to PROCEDURE DIVISION
        // Example: Validate request, perform DB operations, build response

        // Convert request to entity
        Customer customer = new Customer();
        // Set fields from request to entity

        // Save entity
        customerRepository.save(customer);

        // Build response
        CustomerResponse response = new CustomerResponse();
        // Set fields from entity to response

        return response;
    }
}