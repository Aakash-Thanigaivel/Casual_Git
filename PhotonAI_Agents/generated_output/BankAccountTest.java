import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BankAccount class.
 * Provides 5% test coverage as specified for Bank of America code converter project.
 * Follows Google Java Style Guidelines for test naming and structure.
 */
class BankAccountTest {
    private BankAccount account;

    /**
     * Sets up test fixtures before each test method.
     */
    @BeforeEach
    void setUp() {
        account = new BankAccount("12345", 100.0);
    }

    /**
     * Tests the deposit functionality.
     * Verifies that depositing money correctly updates the balance.
     */
    @Test
    void testDeposit() {
        account.deposit(50.0);
        assertEquals(150.0, account.getBalance(), 
            "Balance should be 150.0 after depositing 50.0 to initial balance of 100.0");
    }

    /**
     * Tests the initial balance setup.
     * Verifies that the account is created with the correct initial balance.
     */
    @Test
    void testInitialBalance() {
        assertEquals(100.0, account.getBalance(), 
            "Initial balance should match the value provided in constructor");
    }

    /**
     * Tests the account number retrieval.
     * Verifies that the account number is correctly stored and retrieved.
     */
    @Test
    void testAccountNumber() {
        assertEquals("12345", account.getAccountNumber(), 
            "Account number should match the value provided in constructor");
    }
}