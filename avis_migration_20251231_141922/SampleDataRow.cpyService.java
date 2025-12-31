package com.example.sampledata.service;

import com.example.sampledata.dto.SampleDataRequest;
import com.example.sampledata.dto.SampleDataResponse;
import com.example.sampledata.entity.SampleDataEntity;
import com.example.sampledata.repository.SampleDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SampleDataService {

    @Autowired
    private SampleDataRepository sampleDataRepository;

    @Transactional
    public SampleDataResponse processSampleData(SampleDataRequest request) {
        // Business logic equivalent to PROCEDURE DIVISION
        SampleDataEntity entity = new SampleDataEntity();
        // Map request to entity
        // Perform validation and business logic
        sampleDataRepository.save(entity);
        // Build response
        SampleDataResponse response = new SampleDataResponse();
        return response;
    }
}