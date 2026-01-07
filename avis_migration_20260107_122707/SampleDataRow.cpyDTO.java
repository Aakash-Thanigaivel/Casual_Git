package com.example.sampledata.dto;

import jakarta.validation.constraints.NotNull;

public class SampleDataRequest {

    @NotNull
    private String varLp;

    @NotNull
    private Integer varNumber;

    @NotNull
    private Double varDecimal;

    @NotNull
    private String varCurrency;

    // Getters and Setters

    public String getVarLp() {
        return varLp;
    }

    public void setVarLp(String varLp) {
        this.varLp = varLp;
    }

    public Integer getVarNumber() {
        return varNumber;
    }

    public void setVarNumber(Integer varNumber) {
        this.varNumber = varNumber;
    }

    public Double getVarDecimal() {
        return varDecimal;
    }

    public void setVarDecimal(Double varDecimal) {
        this.varDecimal = varDecimal;
    }

    public String getVarCurrency() {
        return varCurrency;
    }

    public void setVarCurrency(String varCurrency) {
        this.varCurrency = varCurrency;
    }
}

public class SampleDataResponse {

    private String message;

    // Getters and Setters

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}