// Translated from https://phtnaistoragedev.blob.core.windows.net/avis/Casual_Git_061330bc_1768556285/SQLSTATE-CHECK.cpy (Spring Boot Fallback)
// Java 21 | Spring Boot 3.3
package com.migration.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.migration.service.SQLSTATE-CHECKService;
import com.migration.dto.SQLSTATE-CHECKRequest;
import com.migration.dto.SQLSTATE-CHECKResponse;

@RestController
@RequestMapping("/api/sqlstate-check")
public class SQLSTATE-CHECKController {

    @Autowired
    private SQLSTATE-CHECKService service;

    @PostMapping
    public SQLSTATE-CHECKResponse processSQLSTATE-CHECK(@RequestBody SQLSTATE-CHECKRequest request) {
        return service.process(request);
    }
}
