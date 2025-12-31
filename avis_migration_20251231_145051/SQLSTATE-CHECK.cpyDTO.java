package com.example.sqlstatecheck.dto;

import jakarta.validation.constraints.NotNull;

public class RequestDto {

    @NotNull
    private String inputField;

    // Getters and setters

    public String getInputField() {
        return inputField;
    }

    public void setInputField(String inputField) {
        this.inputField = inputField;
    }
}

public class ResponseDto {

    private String outputField;

    // Getters and setters

    public String getOutputField() {
        return outputField;
    }

    public void setOutputField(String outputField) {
        this.outputField = outputField;
    }
}