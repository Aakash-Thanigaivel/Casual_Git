package com.example.sampledata.controller;

import com.example.sampledata.dto.SampleDataRequest;
import com.example.sampledata.dto.SampleDataResponse;
import com.example.sampledata.service.SampleDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class SampleDataController {

    @Autowired
    private SampleDataService sampleDataService;

    @PostMapping("/sample-data")
    public ResponseEntity<SampleDataResponse> processSampleData(@Valid @RequestBody SampleDataRequest request) {
        SampleDataResponse response = sampleDataService.processRequest(request);
        return ResponseEntity.ok(response);
    }
}