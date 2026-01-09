// Translated from https://phtnaistoragedev.blob.core.windows.net/avis/Casual_Git_061330bc_1767970581/SampleDataRow.cpy (Spring Boot Fallback)
// Java 21 | Spring Boot 3.3
package com.migration.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.migration.service.SampleDataRowService;
import com.migration.dto.SampleDataRowRequest;
import com.migration.dto.SampleDataRowResponse;

@RestController
@RequestMapping("/api/sampledatarow")
public class SampleDataRowController {

    @Autowired
    private SampleDataRowService service;

    @PostMapping
    public SampleDataRowResponse processSampleDataRow(@RequestBody SampleDataRowRequest request) {
        return service.process(request);
    }
}
