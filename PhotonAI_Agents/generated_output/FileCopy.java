package com.bofa.filecopy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;

/**
 * Java equivalent of the COBOL FILECOPY program.
 *
 * <p>This class represents a converted COBOL program that copies one sequential file to another.
 * The conversion preserves the original file processing logic and error handling while using
 * modern Java practices and JDK 17 features.
 *
 * <p>Original function: Copy one sequential file to another with record processing and validation.
 */
public final class FileCopy {

  private static final String INPUT_FILE_NAME = "INFILE";
  private static final String OUTPUT_FILE_NAME = "OUTFILE";
  private static final int RECORD_LENGTH = 40;
  private static final DecimalFormat COUNT_FORMATTER = new DecimalFormat("##,##0");

  /**
   * Represents an input record structure equivalent to INPUT-RECORD in COBOL.
   */
  public static final class InputRecord {
    // Equivalent to IN-FIELD-1 PIC X(10)
    private String inField1;
    
    // Equivalent to FILLER PIC X(20) - not used but maintains structure
    private String filler;
    
    // Equivalent to IN-FIELD-2 PIC X(10)
    private String inField2;

    /**
     * Creates an input record with empty fields.
     */
    public InputRecord() {
      this.inField1 = "";
      this.filler = "";
      this.inField2 = "";
    }

    /**
     * Creates an input record from a 40-character string.
     *
     * @param recordData the 40-character record data
     */
    public InputRecord(String recordData) {
      parseRecord(recordData);
    }

    /**
     * Parses a 40-character record into fields.
     *
     * @param recordData the record data to parse
     */
    private void parseRecord(String recordData) {
      if (recordData == null) {
        recordData = "";
      }
      
      // Pad to 40 characters if necessary
      var paddedData = String.format("%-40s", recordData);
      
      this.inField1 = paddedData.substring(0, 10).trim();
      this.filler = paddedData.substring(10, 30);
      this.inField2 = paddedData.substring(30, 40).trim();
    }

    public String getInField1() {
      return inField1;
    }

    public void setInField1(String inField1) {
      this.inField1 = truncateToLength(inField1, 10);
    }

    public String getFiller() {
      return filler;
    }

    public String getInField2() {
      return inField2;
    }

    public void setInField2(String inField2) {
      this.inField2 = truncateToLength(inField2, 10);
    }

    private static String truncateToLength(String value, int maxLength) {
      if (value == null) {
        return "";
      }
      return value.length() > maxLength ? value.substring(0, maxLength) : value;
    }
  }

  /**
   * Represents an output record structure equivalent to OUTPUT-RECORD in COBOL.
   */
  public static final class OutputRecord {
    // Equivalent to OUT-FIELD-1 (from OUTREC copy)
    private String outField1;
    
    // Equivalent to OUT-FIELD-2 (from OUTREC copy)
    private String outField2;
    
    // Equivalent to OUT-FIELD-3 (from OUTREC copy)
    private String outField3;

    /**
     * Creates an output record with empty fields.
     */
    public OutputRecord() {
      this.outField1 = "";
      this.outField2 = "";
      this.outField3 = "";
    }

    public String getOutField1() {
      return outField1;
    }

    public void setOutField1(String outField1) {
      this.outField1 = truncateToLength(outField1, 10);
    }

    public String getOutField2() {
      return outField2;
    }

    public void setOutField2(String outField2) {
      this.outField2 = truncateToLength(outField2, 10);
    }

    public String getOutField3() {
      return outField3;
    }

    public void setOutField3(String outField3) {
      this.outField3 = truncateToLength(outField3, 20);
    }

    /**
     * Formats the output record as a 40-character string.
     *
     * @return the formatted record string
     */
    public String toRecordString() {
      return String.format("%-10s%-20s%-10s", 
          outField1, outField3, outField2);
    }

    private static String truncateToLength(String value, int maxLength) {
      if (value == null) {
        return "";
      }
      return value.length() > maxLength ? value.substring(0, maxLength) : value;
    }
  }

  /**
   * File status enumeration equivalent to COBOL file status values.
   */
  public enum FileStatus {
    OK("00"),
    END_OF_FILE("10"),
    FILE_NOT_FOUND("35"),
    OTHER("99");

    private final String code;

    FileStatus(String code) {
      this.code = code;
    }

    public String getCode() {
      return code;
    }
  }

  /**
   * Working storage equivalent to WORKING-STORAGE SECTION in COBOL.
   */
  public static final class WorkingStorage {
    private FileStatus inputFileStatus;
    private FileStatus outputFileStatus;
    private int wsCount;
    private String wsCountFormatted;
    private String wsErrorMessage;

    /**
     * Initializes working storage with default values.
     */
    public WorkingStorage() {
      this.inputFileStatus = FileStatus.OK;
      this.outputFileStatus = FileStatus.OK;
      this.wsCount = 0;
      this.wsCountFormatted = "";
      this.wsErrorMessage = "";
    }

    // Getters and setters with validation

    public FileStatus getInputFileStatus() {
      return inputFileStatus;
    }

    public void setInputFileStatus(FileStatus inputFileStatus) {
      this.inputFileStatus = inputFileStatus != null ? inputFileStatus : FileStatus.OTHER;
    }

    public FileStatus getOutputFileStatus() {
      return outputFileStatus;
    }

    public void setOutputFileStatus(FileStatus outputFileStatus) {
      this.outputFileStatus = outputFileStatus != null ? outputFileStatus : FileStatus.OTHER;
    }

    public int getWsCount() {
      return wsCount;
    }

    public void setWsCount(int wsCount) {
      // COBOL PIC S9(5) COMP-3 allows values -99999 to 99999
      if (wsCount < -99999 || wsCount > 99999) {
        throw new IllegalArgumentException("Count must be between -99999 and 99999");
      }
      this.wsCount = wsCount;
      this.wsCountFormatted = COUNT_FORMATTER.format(wsCount);
    }

    public String getWsCountFormatted() {
      return wsCountFormatted;
    }

    public String getWsErrorMessage() {
      return wsErrorMessage;
    }

    public void setWsErrorMessage(String wsErrorMessage) {
      this.wsErrorMessage = truncateToLength(wsErrorMessage, 60);
    }

    private static String truncateToLength(String value, int maxLength) {
      if (value == null) {
        return "";
      }
      return value.length() > maxLength ? value.substring(0, maxLength) : value;
    }
  }

  private final WorkingStorage workingStorage;
  private BufferedReader inputReader;
  private BufferedWriter outputWriter;

  /**
   * Creates a new FileCopy program instance.
   */
  public FileCopy() {
    this.workingStorage = new WorkingStorage();
  }

  /**
   * Gets the working storage instance.
   *
   * @return the working storage containing program variables
   */
  public WorkingStorage getWorkingStorage() {
    return workingStorage;
  }

  /**
   * Executes the main program logic.
   * Equivalent to the PROCEDURE DIVISION in the original COBOL program.
   */
  public void execute() {
    try {
      initialize();
      process();
      housekeeping();
    } catch (Exception e) {
      workingStorage.setWsErrorMessage("Unexpected error: " + e.getMessage());
      abort();
    }
  }

  /**
   * Initializes the program - equivalent to 1000-INITIALIZE section.
   */
  private void initialize() {
    try {
      // Open input file
      var inputPath = Paths.get(INPUT_FILE_NAME);
      if (!Files.exists(inputPath)) {
        workingStorage.setInputFileStatus(FileStatus.FILE_NOT_FOUND);
        workingStorage.setWsErrorMessage("Input file not found");
        abort();
        return;
      }

      inputReader = Files.newBufferedReader(inputPath);
      workingStorage.setInputFileStatus(FileStatus.OK);

    } catch (FileNotFoundException e) {
      workingStorage.setInputFileStatus(FileStatus.FILE_NOT_FOUND);
      workingStorage.setWsErrorMessage("Input file not found");
      abort();
      return;
    } catch (IOException e) {
      workingStorage.setInputFileStatus(FileStatus.OTHER);
      workingStorage.setWsErrorMessage("Unexpected input file status on open " + e.getMessage());
      abort();
      return;
    }

    try {
      // Open output file
      var outputPath = Paths.get(OUTPUT_FILE_NAME);
      outputWriter = Files.newBufferedWriter(outputPath);
      workingStorage.setOutputFileStatus(FileStatus.OK);

    } catch (IOException e) {
      workingStorage.setOutputFileStatus(FileStatus.OTHER);
      workingStorage.setWsErrorMessage("Unexpected output file status on open " + e.getMessage());
      abort();
      return;
    }

    workingStorage.setWsCount(0);
  }

  /**
   * Main processing loop - equivalent to 5000-PROCESS section.
   */
  private void process() {
    try {
      String line;
      while ((line = inputReader.readLine()) != null) {
        var inputRecord = new InputRecord(line);
        var outputRecord = prepareOutputRecord(inputRecord);
        writeOutputRecord(outputRecord);
      }
      workingStorage.setInputFileStatus(FileStatus.END_OF_FILE);
    } catch (IOException e) {
      workingStorage.setWsErrorMessage("Error reading input file: " + e.getMessage());
      abort();
    }
  }

  /**
   * Prepares output record - equivalent to 5200-PREPARE-OUTPUT-RECORD section.
   *
   * @param inputRecord the input record to process
   * @return the prepared output record
   */
  private OutputRecord prepareOutputRecord(InputRecord inputRecord) {
    var outputRecord = new OutputRecord();
    outputRecord.setOutField1(inputRecord.getInField1());
    outputRecord.setOutField2(inputRecord.getInField2());
    outputRecord.setOutField3("Good");
    return outputRecord;
  }

  /**
   * Writes output record - equivalent to 5400-WRITE-OUTPUT-RECORD section.
   *
   * @param outputRecord the record to write
   */
  private void writeOutputRecord(OutputRecord outputRecord) {
    try {
      outputWriter.write(outputRecord.toRecordString());
      outputWriter.newLine();
      workingStorage.setOutputFileStatus(FileStatus.OK);
      workingStorage.setWsCount(workingStorage.getWsCount() + 1);
    } catch (IOException e) {
      workingStorage.setOutputFileStatus(FileStatus.OTHER);
      workingStorage.setWsErrorMessage("Unexpected output file status on write " + e.getMessage());
      abort();
    }
  }

  /**
   * Cleanup operations - equivalent to 8000-HOUSEKEEPING section.
   */
  private void housekeeping() {
    try {
      if (outputWriter != null) {
        outputWriter.close();
      }
      if (inputReader != null) {
        inputReader.close();
      }
      
      System.out.println("Records processed: " + workingStorage.getWsCountFormatted());
      
    } catch (IOException e) {
      System.err.println("Error closing files: " + e.getMessage());
    }
  }

  /**
   * Error handling - equivalent to 9999-ABORT section.
   */
  private void abort() {
    System.err.println(workingStorage.getWsErrorMessage());
    
    try {
      if (outputWriter != null) {
        outputWriter.close();
      }
      if (inputReader != null) {
        inputReader.close();
      }
    } catch (IOException e) {
      System.err.println("Error closing files during abort: " + e.getMessage());
    }
  }

  /**
   * Main method to run the FileCopy program.
   *
   * @param args command line arguments (not used)
   */
  public static void main(String[] args) {
    var fileCopy = new FileCopy();
    fileCopy.execute();
  }
}