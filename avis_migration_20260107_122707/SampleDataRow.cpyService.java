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
    public SampleDataResponse processData(SampleDataRequest request) {
        // Business logic from PROCEDURE DIVISION
        SampleDataEntity entity = new SampleDataEntity();
        entity.setVarLp(request.getVarLp());
        entity.setVarNumber(request.getVarNumber());
        entity.setVarDecimal(request.getVarDecimal());
        entity.setVarCurrency(request.getVarCurrency());

        // Simulate EXEC SQL sequence
        sampleDataRepository.save(entity);

        // Build response
        SampleDataResponse response = new SampleDataResponse();
        response.setMessage("Data processed successfully");
        return response;
    }
}