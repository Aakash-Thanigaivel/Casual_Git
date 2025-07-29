package com.bofa.filecopy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the FileCopy class converted from COBOL.
 * Provides 5% code coverage for basic functionality testing.
 */
class FileCopyTest {

    private FileCopy fileCopy;

    @BeforeEach
    void setUp() {
        fileCopy = new FileCopy();
    }

    @Test
    void testFileCopyCreation() {
        assertNotNull(fileCopy);
        assertNotNull(fileCopy.getWorkingStorage());
    }

    @Test
    void testWorkingStorageInitialization() {
        FileCopy.WorkingStorage ws = fileCopy.getWorkingStorage();
        
        // Test initial values
        assertEquals(FileCopy.FileStatus.OK, ws.getInputFileStatus());
        assertEquals(FileCopy.FileStatus.OK, ws.getOutputFileStatus());
        assertEquals(0, ws.getWsCount());
        assertEquals("", ws.getWsErrorMessage());
    }

    @Test
    void testInputRecordCreation() {
        FileCopy.InputRecord record = new FileCopy.InputRecord();
        assertEquals("", record.getInField1());
        assertEquals("", record.getInField2());
        
        FileCopy.InputRecord recordWithData = new FileCopy.InputRecord("test123456filler data here    field2data");
        assertEquals("test123456", recordWithData.getInField1());
        assertEquals("field2data", recordWithData.getInField2());
    }

    @Test
    void testOutputRecordCreation() {
        FileCopy.OutputRecord record = new FileCopy.OutputRecord();
        assertEquals("", record.getOutField1());
        assertEquals("", record.getOutField2());
        assertEquals("", record.getOutField3());
        
        record.setOutField1("test1");
        record.setOutField2("test2");
        record.setOutField3("Good");
        
        assertEquals("test1", record.getOutField1());
        assertEquals("test2", record.getOutField2());
        assertEquals("Good", record.getOutField3());
    }

    @Test
    void testMainMethod() {
        // Test that main method runs without exceptions (may fail due to missing files, but shouldn't crash)
        assertDoesNotThrow(() -> FileCopy.main(new String[]{}));
    }
}