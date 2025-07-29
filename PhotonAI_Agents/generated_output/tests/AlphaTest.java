package com.bofa.alpha;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Unit tests for Alpha class.
 * Tests basic functionality of the converted COBOL ALPHA program.
 */
@DisplayName("Alpha Class Tests")
class AlphaTest {

    private Alpha alpha;

    @BeforeEach
    void setUp() {
        alpha = new Alpha();
    }

    @Test
    @DisplayName("Test Alpha constructor initialization")
    void testConstructorInitialization() {
        assertNotNull(alpha);
        assertEquals("", alpha.getWsField1());
        assertEquals("", alpha.getWsField2());
        assertEquals(0, alpha.getWsDisplayNumeric());
        assertNotNull(alpha.getWsTable1());
        assertNotNull(alpha.getWsTable2());
        assertEquals(5, alpha.getWsTable1().size());
        assertEquals(5, alpha.getWsTable2().size());
    }

    @Test
    @DisplayName("Test execute method")
    void testExecuteMethod() {
        assertDoesNotThrow(() -> alpha.execute());
    }

    @Test
    @DisplayName("Test main method")
    void testMainMethod() {
        assertDoesNotThrow(() -> Alpha.main(new String[]{}));
    }

    @Test
    @DisplayName("Test field setters and getters")
    void testFieldSettersAndGetters() {
        alpha.setWsField1("TEST1");
        alpha.setWsField2("TEST2");
        alpha.setWsDisplayNumeric(123);

        assertEquals("TEST1", alpha.getWsField1());
        assertEquals("TEST2", alpha.getWsField2());
        assertEquals(123, alpha.getWsDisplayNumeric());
    }

    @Test
    @DisplayName("Test TableEntry1 functionality")
    void testTableEntry1() {
        Alpha.TableEntry1 entry = new Alpha.TableEntry1();
        entry.setWsThing1("THING1");
        entry.setWsThing2("THING2");

        assertEquals("THING1", entry.getWsThing1());
        assertEquals("THING2", entry.getWsThing2());
    }
}