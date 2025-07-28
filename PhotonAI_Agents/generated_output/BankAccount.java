import java.util.List;
import java.util.ArrayList;

/**
 * Bank account class for Bank of America code converter project.
 * Converted from C++ to Java following Google Java Style Guidelines.
 */
public class BankAccount {
    private double balance;
    private String accountNumber;

    /**
     * Constructs a new bank account with specified account number and initial balance.
     * 
     * @param accountNumber The unique account identifier
     * @param initialBalance The starting balance for the account
     */
    public BankAccount(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }
    
    /**
     * Deposits the specified amount into the account.
     * 
     * @param amount The amount to deposit (must be positive)
     */
    public void deposit(double amount) {
        this.balance += amount;
    }
    
    /**
     * Returns the current account balance.
     * 
     * @return The current balance
     */
    public double getBalance() {
        return this.balance;
    }
    
    /**
     * Returns the account number.
     * 
     * @return The account number
     */
    public String getAccountNumber() {
        return this.accountNumber;
    }
}