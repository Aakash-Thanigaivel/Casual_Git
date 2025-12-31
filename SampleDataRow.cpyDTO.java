package com.example.sampledatarow.dto;

import jakarta.validation.constraints.NotNull;

public class SampleDataRowRequest {

    @NotNull
    private String filler1;

    @NotNull
    private String filler2;

    @NotNull
    private String filler3;

    // Getters and setters
}

public class SampleDataRowResponse {

    private String responseMessage;

    // Getters and setters
}