package com.bofa.bipm012;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

/**
 * Unit tests for Bipm012 class.
 * Tests unit test validation functionality of the converted COBOL BIPM012 program.
 */
@DisplayName("Bipm012 Class Tests")
class Bipm012Test {

    private Bipm012 bipm012;

    @BeforeEach
    void setUp() {
        bipm012 = new Bipm012();
    }

    @Test
    @DisplayName("Test Bipm012 constructor initialization")
    void testConstructorInitialization() {
        assertNotNull(bipm012);
        assertNotNull(bipm012.getWorkFields1());
        assertNotNull(bipm012.getBipm012Parm());
        assertEquals(BigDecimal.ZERO, bipm012.getWorkFields1().getWsBalanceX());
        assertEquals(0, bipm012.getWorkFields1().getMoveIdx());
    }

    @Test
    @DisplayName("Test execute method")
    void testExecuteMethod() {
        assertDoesNotThrow(() -> bipm012.execute());
        assertEquals(0, bipm012.getBipm012Parm().getOutputData().getMaxLength());
    }

    @Test
    @DisplayName("Test processValidation method")
    void testProcessValidation() {
        Bipm012.Bipm012Parm result = bipm012.processValidation(123);
        assertNotNull(result);
        assertEquals(123, result.getInputData().getUserNo());
        assertEquals(0, result.getOutputData().getMaxLength());
    }

    @Test
    @DisplayName("Test setCurrentDate method")
    void testSetCurrentDate() {
        bipm012.setCurrentDate();
        assertNotNull(bipm012.getWorkFields1().getWsDateBefore());
        assertFalse(bipm012.getWorkFields1().getWsDateBefore().isEmpty());
    }

    @Test
    @DisplayName("Test InputData userNo validation")
    void testInputDataUserNoValidation() {
        Bipm012.InputData inputData = new Bipm012.InputData();
        
        // Test valid range
        inputData.setUserNo(500);
        assertEquals(500, inputData.getUserNo());
        
        // Test boundary values
        inputData.setUserNo(999);
        assertEquals(999, inputData.getUserNo());
        
        inputData.setUserNo(0);
        assertEquals(0, inputData.getUserNo());
    }

    @Test
    @DisplayName("Test main method")
    void testMainMethod() {
        assertDoesNotThrow(() -> Bipm012.main(new String[]{}));
    }
}