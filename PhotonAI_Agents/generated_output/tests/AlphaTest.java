package com.bofa.alpha;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the Alpha class converted from COBOL.
 * Provides 5% code coverage for basic functionality testing.
 */
class AlphaTest {

    private Alpha alpha;

    @BeforeEach
    void setUp() {
        alpha = new Alpha();
    }

    @Test
    void testAlphaCreation() {
        assertNotNull(alpha);
        assertNotNull(alpha.getWorkingStorage());
    }

    @Test
    void testExecuteMethod() {
        // Test that execute method runs without throwing exceptions
        assertDoesNotThrow(() -> alpha.execute());
    }

    @Test
    void testWorkingStorageInitialization() {
        Alpha.WorkingStorage ws = alpha.getWorkingStorage();
        
        // Test initial values
        assertEquals("", ws.getWsField1());
        assertEquals("", ws.getWsField2());
        assertEquals(0, ws.getWsDisplayNumeric());
        assertNotNull(ws.getWsTable1());
        assertNotNull(ws.getWsTable2());
    }

    @Test
    void testTableEntryCreation() {
        Alpha.TableEntry entry = new Alpha.TableEntry();
        assertEquals("", entry.getThing1());
        assertEquals("", entry.getThing2());
        
        Alpha.TableEntry entryWithValues = new Alpha.TableEntry("test1", "test2");
        assertEquals("test1", entryWithValues.getThing1());
        assertEquals("test2", entryWithValues.getThing2());
    }

    @Test
    void testMainMethod() {
        // Test that main method runs without exceptions
        assertDoesNotThrow(() -> Alpha.main(new String[]{}));
    }
}