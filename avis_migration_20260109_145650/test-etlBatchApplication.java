// Translated from https://phtnaistoragedev.blob.core.windows.net/avis/Casual_Git_061330bc_1767970581/test-etl.cbl (Batch ETL Fallback)
// Java 21
package com.batch.etl;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import com.batch.etl.model.DataModel.*;
import com.batch.etl.BusinessLogic;
import com.batch.etl.FileProcessor;

public class test-etlBatchApplication {

    private static final String INPUT_FILE = "test-etl_input.txt";
    private static final String VALID_OUTPUT_FILE = "test-etl_valid.txt";
    private static final String REJECT_OUTPUT_FILE = "test-etl_reject.txt";

    public static void main(String[] args) {
        System.out.println("Starting test-etl ETL Batch Process");

        try {
            processEtlBatch();
            System.out.println("ETL Batch Process completed successfully");
        } catch (Exception e) {
            System.err.println("ETL Batch Process failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void processEtlBatch() throws IOException {
        Path inputPath = Paths.get(INPUT_FILE);
        Path validOutputPath = Paths.get(VALID_OUTPUT_FILE);
        Path rejectOutputPath = Paths.get(REJECT_OUTPUT_FILE);

        try (BufferedReader reader = Files.newBufferedReader(inputPath);
             BufferedWriter validWriter = Files.newBufferedWriter(validOutputPath);
             BufferedWriter rejectWriter = Files.newBufferedWriter(rejectOutputPath)) {

            String line;
            boolean eof = false;

            while (!eof && (line = reader.readLine()) != null) {
                try {
                    // Parse input record using DataModel
                    InputRecord input = InputRecord.parse(line.trim());

                    // Validate using BusinessLogic
                    if (!BusinessLogic.validateRecord(input)) {
                        // Write to reject file using DataModel
                        RejectRecord reject = new RejectRecord(input.getIdentifier(), "INVALID RECORD", input.toString());
                        FileProcessor.writeRecord(rejectWriter, reject.format());
                    } else {
                        // Process valid transaction using BusinessLogic
                        OutputRecord output = BusinessLogic.processTransaction(input);
                        FileProcessor.writeRecord(validWriter, output.format());
                    }

                } catch (Exception e) {
                    System.err.println("Error processing record: " + line + " - " + e.getMessage());
                    // Write to reject file with parsing error using DataModel
                    RejectRecord reject = new RejectRecord("UNKNOWN", "PARSE ERROR", line);
                    FileProcessor.writeRecord(rejectWriter, reject.format());
                }
            }
        }
    }
}
