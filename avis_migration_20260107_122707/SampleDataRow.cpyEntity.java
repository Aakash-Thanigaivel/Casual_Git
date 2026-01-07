package com.example.sampledata.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sample_data")
public class SampleDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "var_lp")
    private String varLp;

    @Column(name = "var_number")
    private Integer varNumber;

    @Column(name = "var_decimal")
    private Double varDecimal;

    @Column(name = "var_currency")
    private String varCurrency;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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