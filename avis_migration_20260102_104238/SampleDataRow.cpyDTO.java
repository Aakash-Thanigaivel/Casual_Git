package com.example.sampledata.dto;

import jakarta.validation.constraints.NotNull;

public class SampleDataRequest {

    @NotNull
    private String filler1;

    @NotNull
    private String filler2;

    @NotNull
    private String filler3;

    // Getters and Setters
}

public class SampleDataResponse {

    private String responseMessage;

    // Getters and Setters
}