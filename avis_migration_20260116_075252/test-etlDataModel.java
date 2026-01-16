// Data model classes dynamically generated from COBOL FD sections - Banking Domain
package com.batch.etl.model;

import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;

public class DataModel {
    // Banking input file record structure (from COBOL FD)
    public record InputRecord(
        String accountNumber,  // PIC X(12) - Account number
        String transType,      // PIC X(8) - Transaction type (DEPOSIT/WITHDRAW)
        String transDate,      // PIC 9(8) - Transaction date (YYYYMMDD)
        BigDecimal amount,      // PIC 9(9)V99 - Transaction amount
        BigDecimal currentBalance // PIC 9(12)V99 - Current balance
    ) {
        public static InputRecord parse(String line) {
            if (line == null || line.length() < 35) { 
                throw new IllegalArgumentException("Invalid record length: expected at least 35 characters"); 
            }
            
            try {
                // Parse according to COBOL FD structure
                String acctNum = line.substring(0, 12).trim();
                String transactionType = line.substring(12, 20).trim();
                String transactionDate = line.substring(20, 28).trim();
                String amountStr = line.substring(28, 37).trim();
                String balanceStr = line.substring(37, 49).trim();
                
                // Convert numeric strings to BigDecimal
                BigDecimal transactionAmount = new BigDecimal(amountStr).divide(BigDecimal.valueOf(100));
                BigDecimal currentBal = new BigDecimal(balanceStr).divide(BigDecimal.valueOf(100));
                
                return new InputRecord(acctNum, transactionType, transactionDate, transactionAmount, currentBal);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid numeric format in record: " + line, e);
            } catch (Exception e) {
                throw new IllegalArgumentException("Error parsing record: " + line, e);
            }
        }
        
        public String getIdentifier() {
            return accountNumber;
        }
        
        @Override
        public String toString() {
            return String.format("InputRecord[account=%s, type=%s, date=%s, amount=%.2f, balance=%.2f]", 
                accountNumber, transType, transDate, amount, currentBalance);
        }
    }

    // Banking output file record structure (from COBOL FD)
    public record OutputRecord(
        String accountNumber,  // PIC X(12) - Account number
        String transType,      // PIC X(8) - Transaction type
        BigDecimal newBalance, // PIC 9(12)V99 - New balance
        BigDecimal amount       // PIC 9(9)V99 - Transaction amount
    ) {
        public String format() {
            // Format according to COBOL output structure
            // PIC X(12) + PIC X(8) + PIC 9(12)V99 + PIC 9(9)V99
            return String.format("%-12.12s%-8.8s%014.2f%011.2f",
                accountNumber, transType, newBalance, amount);
        }
        
        @Override
        public String toString() {
            return String.format("OutputRecord[account=%s, type=%s, newBalance=%.2f, amount=%.2f]", 
                accountNumber, transType, newBalance, amount);
        }
    }

    // Error file record structure
    public record RejectRecord(
        String identifier,      // Record identifier or error indicator
        String reason,          // Rejection reason
        String originalRecord    // Original input record for debugging
    ) {
        public String format() {
            // Format for error file (80 character width)
            return String.format("%-80s", originalRecord != null ? originalRecord : reason);
        }
        
        @Override
        public String toString() {
            return String.format("RejectRecord[id=%s, reason=%s, original=%s]", 
                identifier, reason, originalRecord);
        }
    }

    // Processing summary record
    public record SummaryRecord(
        int totalRecords,
        int validRecords,
        int rejectRecords,
        BigDecimal totalAmount,
        LocalDate processTime
    ) {
        public String format() {
            return String.format("SUMMARY: Total=%d, Valid=%d, Rejected=%d, TotalAmount=%.2f, Time=%s",
                totalRecords, validRecords, rejectRecords, totalAmount, 
                processTime.format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
    }
}
