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
        // Example: Fetch customer, validate, process, and respond
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Perform business processing and validation
        // ...

        // Build response
        CustomerResponse response = new CustomerResponse();
        response.setCustomerId(customer.getId());
        response.setStatus("Processed");
        return response;
    }
}