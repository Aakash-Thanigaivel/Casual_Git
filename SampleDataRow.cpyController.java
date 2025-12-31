package com.example.sampledatarow.controller;

import com.example.sampledatarow.dto.SampleDataRowRequest;
import com.example.sampledatarow.dto.SampleDataRowResponse;
import com.example.sampledatarow.service.SampleDataRowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
public class SampleDataRowController {

    @Autowired
    private SampleDataRowService sampleDataRowService;

    @PostMapping("/sampledatarow")
    public ResponseEntity<SampleDataRowResponse> processSampleDataRow(@Valid @RequestBody SampleDataRowRequest request) {
        SampleDataRowResponse response = sampleDataRowService.processRequest(request);
        return ResponseEntity.ok(response);
    }
}