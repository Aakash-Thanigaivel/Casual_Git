package com.bofa.filecopy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;

/**
 * Java equivalent of COBOL FILECOPY program.
 * Copy one sequential file to another with data transformation.
 * 
 * Following Google Java Style Guidelines as specified in P13456-Java - Coding Standard - Guide Lines-190625-095145.pdf
 */
public class FileCopy {
    
    // Constants
    private static final String INPUT_FILE_NAME = "INFILE";
    private static final String OUTPUT_FILE_NAME = "OUTFILE";
    private static final int RECORD_LENGTH = 40;
    private static final String DEFAULT_FIELD_3_VALUE = "Good";
    
    // File status constants
    private static final String STATUS_OK = "00";
    private static final String STATUS_END_OF_FILE = "10";
    private static final String STATUS_FILE_NOT_FOUND = "35";
    
    // Working storage equivalent - instance variables
    private String inputFileStatus;
    private String outputFileStatus;
    private int wsCount;
    private String wsCountFormatted;
    private String wsErrorMessage;
    
    // File handles
    private BufferedReader inputReader;
    private BufferedWriter outputWriter;
    
    /**
     * Input record structure.
     */
    public static class InputRecord {
        private String inField1;
        private String filler;
        private String inField2;
        
        public InputRecord() {
            this.inField1 = "";
            this.filler = "";
            this.inField2 = "";
        }
        
        public InputRecord(String recordLine) {
            parseRecord(recordLine);
        }
        
        private void parseRecord(String recordLine) {
            if (recordLine == null || recordLine.length() < RECORD_LENGTH) {
                recordLine = String.format("%-40s", recordLine != null ? recordLine : "");
            }
            
            this.inField1 = recordLine.substring(0, 10).trim();
            this.filler = recordLine.substring(10, 30);
            this.inField2 = recordLine.substring(30, 40).trim();
        }
        
        // Getters and setters
        public String getInField1() {
            return inField1;
        }
        
        public void setInField1(String inField1) {
            this.inField1 = inField1 != null ? inField1 : "";
        }
        
        public String getFiller() {
            return filler;
        }
        
        public void setFiller(String filler) {
            this.filler = filler != null ? filler : "";
        }
        
        public String getInField2() {
            return inField2;
        }
        
        public void setInField2(String inField2) {
            this.inField2 = inField2 != null ? inField2 : "";
        }
    }
    
    /**
     * Output record structure.
     */
    public static class OutputRecord {
        private String outField1;
        private String outField2;
        private String outField3;
        
        public OutputRecord() {
            this.outField1 = "";
            this.outField2 = "";
            this.outField3 = "";
        }
        
        public String formatRecord() {
            return String.format("%-10s%-20s%-10s", 
                outField1 != null ? outField1 : "",
                "", // Filler space
                outField2 != null ? outField2 : "");
        }
        
        // Getters and setters
        public String getOutField1() {
            return outField1;
        }
        
        public void setOutField1(String outField1) {
            this.outField1 = outField1 != null ? outField1 : "";
        }
        
        public String getOutField2() {
            return outField2;
        }
        
        public void setOutField2(String outField2) {
            this.outField2 = outField2 != null ? outField2 : "";
        }
        
        public String getOutField3() {
            return outField3;
        }
        
        public void setOutField3(String outField3) {
            this.outField3 = outField3 != null ? outField3 : "";
        }
    }
    
    /**
     * Constructor initializes all working storage fields.
     */
    public FileCopy() {
        this.inputFileStatus = STATUS_OK;
        this.outputFileStatus = STATUS_OK;
        this.wsCount = 0;
        this.wsCountFormatted = "";
        this.wsErrorMessage = "";
    }
    
    /**
     * Main procedure division equivalent.
     * Executes the main program logic.
     */
    public void execute() {
        try {
            initialize();
            process();
            housekeeping();
        } catch (Exception e) {
            wsErrorMessage = "Unexpected error: " + e.getMessage();
            abort();
        }
    }
    
    /**
     * 1000-INITIALIZE section equivalent.
     * Opens input and output files with error handling.
     */
    private void initialize() {
        try {
            // Open input file
            Path inputPath = Paths.get(INPUT_FILE_NAME);
            if (!Files.exists(inputPath)) {
                inputFileStatus = STATUS_FILE_NOT_FOUND;
                wsErrorMessage = "Input file not found";
                abort();
                return;
            }
            
            inputReader = Files.newBufferedReader(inputPath);
            inputFileStatus = STATUS_OK;
            
        } catch (IOException e) {
            inputFileStatus = "99";
            wsErrorMessage = "Unexpected input file status on open " + inputFileStatus;
            abort();
            return;
        }
        
        try {
            // Open output file
            Path outputPath = Paths.get(OUTPUT_FILE_NAME);
            outputWriter = Files.newBufferedWriter(outputPath);
            outputFileStatus = STATUS_OK;
            
        } catch (IOException e) {
            outputFileStatus = "99";
            wsErrorMessage = "Unexpected output file status on open " + outputFileStatus;
            abort();
            return;
        }
        
        wsCount = 0;
    }
    
    /**
     * 5000-PROCESS section equivalent.
     * Main processing loop to read, transform, and write records.
     */
    private void process() {
        try {
            String inputLine = inputReader.readLine();
            
            while (inputLine != null) {
                var inputRecord = new InputRecord(inputLine);
                prepareOutputRecord(inputRecord);
                writeOutputRecord();
                inputLine = inputReader.readLine();
            }
            
            inputFileStatus = STATUS_END_OF_FILE;
            
        } catch (IOException e) {
            wsErrorMessage = "Error reading input file: " + e.getMessage();
            abort();
        }
    }
    
    /**
     * 5200-PREPARE-OUTPUT-RECORD section equivalent.
     * Transforms input record to output record format.
     */
    private void prepareOutputRecord(InputRecord inputRecord) {
        var outputRecord = new OutputRecord();
        outputRecord.setOutField1(inputRecord.getInField1());
        outputRecord.setOutField2(inputRecord.getInField2());
        outputRecord.setOutField3(DEFAULT_FIELD_3_VALUE);
        
        // Store for writing
        this.currentOutputRecord = outputRecord;
    }
    
    private OutputRecord currentOutputRecord;
    
    /**
     * 5400-WRITE-OUTPUT-RECORD section equivalent.
     * Writes output record with error checking.
     */
    private void writeOutputRecord() {
        try {
            if (currentOutputRecord != null) {
                outputWriter.write(currentOutputRecord.formatRecord());
                outputWriter.newLine();
                outputFileStatus = STATUS_OK;
                wsCount++;
            }
        } catch (IOException e) {
            outputFileStatus = "99";
            wsErrorMessage = "Unexpected output file status on write " + outputFileStatus;
            abort();
        }
    }
    
    /**
     * 8000-HOUSEKEEPING section equivalent.
     * Closes files and displays processing statistics.
     */
    private void housekeeping() {
        try {
            if (outputWriter != null) {
                outputWriter.close();
            }
            if (inputReader != null) {
                inputReader.close();
            }
            
            var formatter = new DecimalFormat("#,##0");
            wsCountFormatted = formatter.format(wsCount);
            System.out.println("Records processed: " + wsCountFormatted);
            
        } catch (IOException e) {
            System.err.println("Error closing files: " + e.getMessage());
        }
    }
    
    /**
     * 9999-ABORT section equivalent.
     * Centralized error handling and program termination.
     */
    private void abort() {
        System.err.println(wsErrorMessage);
        
        // Clean up resources
        try {
            if (outputWriter != null) {
                outputWriter.close();
            }
            if (inputReader != null) {
                inputReader.close();
            }
        } catch (IOException e) {
            System.err.println("Error during cleanup: " + e.getMessage());
        }
        
        System.exit(1);
    }
    
    /**
     * Utility method to check file status conditions.
     */
    private boolean isInputOk() {
        return STATUS_OK.equals(inputFileStatus);
    }
    
    private boolean isEndOfFile() {
        return STATUS_END_OF_FILE.equals(inputFileStatus);
    }
    
    private boolean isFileNotFound() {
        return STATUS_FILE_NOT_FOUND.equals(inputFileStatus);
    }
    
    private boolean isOutputOk() {
        return STATUS_OK.equals(outputFileStatus);
    }
    
    /**
     * Main method for program execution.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        var fileCopy = new FileCopy();
        fileCopy.execute();
    }
    
    // Getter and setter methods
    public String getInputFileStatus() {
        return inputFileStatus;
    }
    
    public String getOutputFileStatus() {
        return outputFileStatus;
    }
    
    public int getWsCount() {
        return wsCount;
    }
    
    public String getWsErrorMessage() {
        return wsErrorMessage;
    }
}