package com.example.sqlstatecheck.dto;

import jakarta.validation.constraints.NotNull;

public class RequestDto {

    @NotNull
    private String fieldName;

    // Getters and setters

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}

public class ResponseDto {

    private String responseMessage;

    // Getters and setters

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}