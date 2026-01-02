package com.example.testetl.controller;

import com.example.testetl.dto.CustomerRequest;
import com.example.testetl.dto.CustomerResponse;
import com.example.testetl.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/processCustomer")
    public ResponseEntity<CustomerResponse> processCustomer(@Valid @RequestBody CustomerRequest request) {
        CustomerResponse response = customerService.processCustomer(request);
        return ResponseEntity.ok(response);
    }
}