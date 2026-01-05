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
    public CustomerResponse processCustomerRequest(CustomerRequest request) {
        // Business logic equivalent to PROCEDURE DIVISION
        // Example: Fetch customer, validate, process, and build response
        Customer customer = customerRepository.findById(request.getCustomerId()).orElseThrow(() -> new RuntimeException("Customer not found"));
        // Perform business processing
        // Build and return response
        return new CustomerResponse(customer.getId(), customer.getName());
    }
}