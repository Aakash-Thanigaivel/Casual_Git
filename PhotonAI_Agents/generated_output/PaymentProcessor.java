/**
 * Payment processor for Bank of America code converter project.
 * Converted from Java 1.8 to JDK 17 following Google Java Style Guidelines.
 * Uses modern switch expressions introduced in JDK 17.
 */
public class PaymentProcessor {
    
    /**
     * Processes payment using modern JDK 17 switch expressions.
     * Migrated from traditional switch statements to switch expressions.
     * 
     * @param type The payment type (CREDIT, DEBIT, etc.)
     * @param amount The payment amount
     * @return Processing result message
     */
    public String processPayment(String type, double amount) {
        return switch (type) {
            case "CREDIT" -> "Processing credit payment: " + amount;
            case "DEBIT" -> "Processing debit payment: " + amount;
            default -> "Unknown payment type";
        };
    }
}