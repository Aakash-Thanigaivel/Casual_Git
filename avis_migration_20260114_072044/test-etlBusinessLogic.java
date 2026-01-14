// Business logic dynamically generated from COBOL IR - Banking Domain
package com.batch.etl;

import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.batch.etl.model.DataModel.*;

public class BusinessLogic {

    public static boolean validateRecord(InputRecord input) {
        // Domain-specific validation based on IR data (banking)
                // Banking-specific validations
        if (input.amount() != null && input.amount().compareTo(BigDecimal.ZERO) <= 0) {
            return false; // Amount must be positive for transactions
        }
        if (input.accountType() != null && !input.accountType().matches("CHECKING|SAVINGS|CREDIT")) {
            return false; // Invalid account type
        }
        return true; // Default to true if no validation rules
    }
    
    public static OutputRecord processTransaction(InputRecord input) {
        // Domain-specific business logic based on IR data (banking)
                // Banking-specific processing
        BigDecimal interest = BigDecimal.ZERO;
        BigDecimal fee = BigDecimal.ZERO;
        
        if ("DEPOSIT".equals(input.transType())) {
            interest = input.amount().multiply(BigDecimal.valueOf(0.0005)); // Daily interest
        } else if ("WITHDRAW".equals(input.transType())) {
            fee = input.amount().multiply(BigDecimal.valueOf(0.025));
        }
        
        BigDecimal newBalance = input.currentBalance().add(input.amount()).add(interest).subtract(fee);
        
        // Domain-specific output processing
        return _generate_domain_output_banking(input);
    }
    
    
    private static String transformDate(String yyyymmdd) {
        if (yyyymmdd == null || yyyymmdd.length() != 8) {
            return yyyymmdd;
        }
        
        try {
            LocalDate date = LocalDate.parse(yyyymmdd, DateTimeFormatter.ofPattern("yyyyMMdd"));
            return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception e) {
            return yyyymmdd;
        }
    }

    private static OutputRecord _generate_domain_output_banking(InputRecord input) {
        BigDecimal interest = BigDecimal.ZERO;
        BigDecimal fee = BigDecimal.ZERO;
        
        if ("DEPOSIT".equals(input.transType())) {
            interest = input.amount().multiply(BigDecimal.valueOf(0.0005));
        } else if ("WITHDRAW".equals(input.transType())) {
            fee = input.amount().multiply(BigDecimal.valueOf(0.025));
        }
        
        BigDecimal newBalance = input.currentBalance().add(input.amount()).add(interest).subtract(fee);
        
        return new OutputRecord(
            input.accountNumber(),
            input.transType(),
            newBalance,
            input.amount()
        );
    }
    
    public static String formatBankingRecord(String accountNum, String transType, BigDecimal newBalance) {
        return String.format("%-12s%-8s%012.2f", accountNum, transType, newBalance);
    }
}
