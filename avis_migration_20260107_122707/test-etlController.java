package com.example.testetl.controller;

import com.example.testetl.dto.CustomerDataRequest;
import com.example.testetl.dto.CustomerDataResponse;
import com.example.testetl.service.CustomerDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class CustomerDataController {

    @Autowired
    private CustomerDataService customerDataService;

    @PostMapping("/processCustomerData")
    public ResponseEntity<CustomerDataResponse> processCustomerData(@Valid @RequestBody CustomerDataRequest request) {
        CustomerDataResponse response = customerDataService.processCustomerData(request);
        return ResponseEntity.ok(response);
    }
}