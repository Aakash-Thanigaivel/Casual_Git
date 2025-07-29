package com.bofa.bipm012;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Java equivalent of the COBOL BIPM012 program.
 *
 * <p>This class represents a converted COBOL program for unit test validation with REPLACE
 * functionality. The conversion preserves the original data structures and program flow
 * while using modern Java practices and JDK 17 features.
 *
 * <p>Original program: BIPM012 by TNP, dated 20.01.2025
 * Function: Unit test validation with REPLACE operations
 */
public final class Bipm012 {

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  /**
   * Working storage fields equivalent to WORK-FIELDS-1 in COBOL.
   */
  public static final class WorkFields {
    // Equivalent to MOVE-IDX PIC S9(4) COMP
    private short moveIdx;
    
    // Equivalent to WS-BALANCE-X PIC S9(11)V9(2) VALUE 0 COMP
    private BigDecimal wsBalanceX;
    
    // Equivalent to WS-NUMBER-OF-MOVE PIC S9(07) COMP-3
    private int wsNumberOfMove;
    
    // Equivalent to WS-DATE-BEFORE PIC X(10)
    private String wsDateBefore;
    
    // Equivalent to WS-DATE-AFTER PIC X(10)
    private String wsDateAfter;

    /**
     * Initializes work fields with default values.
     */
    public WorkFields() {
      this.moveIdx = 0;
      this.wsBalanceX = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
      this.wsNumberOfMove = 0;
      this.wsDateBefore = "";
      this.wsDateAfter = "";
    }

    public short getMoveIdx() {
      return moveIdx;
    }

    public void setMoveIdx(short moveIdx) {
      // COBOL PIC S9(4) allows values -9999 to 9999
      if (moveIdx < -9999 || moveIdx > 9999) {
        throw new IllegalArgumentException("Move index must be between -9999 and 9999");
      }
      this.moveIdx = moveIdx;
    }

    public BigDecimal getWsBalanceX() {
      return wsBalanceX;
    }

    public void setWsBalanceX(BigDecimal wsBalanceX) {
      if (wsBalanceX == null) {
        this.wsBalanceX = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
      } else {
        // COBOL PIC S9(11)V9(2) - 11 integer digits, 2 decimal places
        this.wsBalanceX = wsBalanceX.setScale(2, RoundingMode.HALF_UP);
      }
    }

    public int getWsNumberOfMove() {
      return wsNumberOfMove;
    }

    public void setWsNumberOfMove(int wsNumberOfMove) {
      // COBOL PIC S9(07) allows values -9999999 to 9999999
      if (wsNumberOfMove < -9999999 || wsNumberOfMove > 9999999) {
        throw new IllegalArgumentException("Number of move must be between -9999999 and 9999999");
      }
      this.wsNumberOfMove = wsNumberOfMove;
    }

    public String getWsDateBefore() {
      return wsDateBefore;
    }

    public void setWsDateBefore(String wsDateBefore) {
      this.wsDateBefore = truncateToLength(wsDateBefore, 10);
    }

    public String getWsDateAfter() {
      return wsDateAfter;
    }

    public void setWsDateAfter(String wsDateAfter) {
      this.wsDateAfter = truncateToLength(wsDateAfter, 10);
    }

    /**
     * Sets date before using LocalDate.
     *
     * @param date the date to set
     */
    public void setWsDateBefore(LocalDate date) {
      this.wsDateBefore = date != null ? date.format(DATE_FORMATTER) : "";
    }

    /**
     * Sets date after using LocalDate.
     *
     * @param date the date to set
     */
    public void setWsDateAfter(LocalDate date) {
      this.wsDateAfter = date != null ? date.format(DATE_FORMATTER) : "";
    }

    private static String truncateToLength(String value, int maxLength) {
      if (value == null) {
        return "";
      }
      return value.length() > maxLength ? value.substring(0, maxLength) : value;
    }
  }

  /**
   * Input data structure equivalent to INPUT-DATA in COBOL.
   */
  public static final class InputData {
    // Equivalent to USERNO PIC 9(03)
    private int userNo;

    /**
     * Creates input data with default values.
     */
    public InputData() {
      this.userNo = 0;
    }

    /**
     * Creates input data with specified user number.
     *
     * @param userNo the user number (0-999)
     */
    public InputData(int userNo) {
      setUserNo(userNo);
    }

    public int getUserNo() {
      return userNo;
    }

    public void setUserNo(int userNo) {
      // COBOL PIC 9(03) allows values 0-999
      if (userNo < 0 || userNo > 999) {
        throw new IllegalArgumentException("User number must be between 0 and 999");
      }
      this.userNo = userNo;
    }
  }

  /**
   * Output data structure equivalent to OUTPUT-DATA in COBOL.
   */
  public static final class OutputData {
    // Equivalent to MAX-HEIGHT usage COMP-3 PIC S9(5)
    private int maxHeight;
    
    // Equivalent to MAX-LENGTH PIC S9(07) usage COMP-3
    private int maxLength;

    /**
     * Creates output data with default values.
     */
    public OutputData() {
      this.maxHeight = 0;
      this.maxLength = 0;
    }

    public int getMaxHeight() {
      return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
      // COBOL PIC S9(5) allows values -99999 to 99999
      if (maxHeight < -99999 || maxHeight > 99999) {
        throw new IllegalArgumentException("Max height must be between -99999 and 99999");
      }
      this.maxHeight = maxHeight;
    }

    public int getMaxLength() {
      return maxLength;
    }

    public void setMaxLength(int maxLength) {
      // COBOL PIC S9(07) allows values -9999999 to 9999999
      if (maxLength < -9999999 || maxLength > 9999999) {
        throw new IllegalArgumentException("Max length must be between -9999999 and 9999999");
      }
      this.maxLength = maxLength;
    }
  }

  /**
   * Parameter structure equivalent to BIPM012-PARM in COBOL.
   */
  public static final class Bipm012Parm {
    private final InputData inputData;
    private final OutputData outputData;

    /**
     * Creates parameter structure with initialized input and output data.
     */
    public Bipm012Parm() {
      this.inputData = new InputData();
      this.outputData = new OutputData();
    }

    public InputData getInputData() {
      return inputData;
    }

    public OutputData getOutputData() {
      return outputData;
    }
  }

  private final WorkFields workFields;
  private final Bipm012Parm bipm012Parm;

  /**
   * Creates a new Bipm012 program instance.
   */
  public Bipm012() {
    this.workFields = new WorkFields();
    this.bipm012Parm = new Bipm012Parm();
  }

  /**
   * Gets the work fields instance.
   *
   * @return the work fields containing program variables
   */
  public WorkFields getWorkFields() {
    return workFields;
  }

  /**
   * Gets the parameter structure instance.
   *
   * @return the parameter structure containing input/output data
   */
  public Bipm012Parm getBipm012Parm() {
    return bipm012Parm;
  }

  /**
   * Executes the main program logic.
   * Equivalent to the PROCEDURE DIVISION in the original COBOL program.
   */
  public void execute() {
    // Equivalent to: MOVE ZERO TO MAX-LENGTH IN :BDSIXXX:-PARM
    // (REPLACE ==:BDSIXXX:== BY ==BIPM012== was applied)
    bipm012Parm.getOutputData().setMaxLength(0);
    
    // Call the move data section
    moveDataSection();
  }

  /**
   * Executes the 100-MOVE-DATA section.
   * Equivalent to the 100-MOVE-DATA SECTION in the original COBOL program.
   */
  private void moveDataSection() {
    // Equivalent to: MOVE WS-DATE-BEFORE TO WS-DATE-AFTER
    workFields.setWsDateAfter(workFields.getWsDateBefore());
  }

  /**
   * Utility method to move date from before to after using LocalDate objects.
   */
  public void moveDateBeforeToAfter() {
    if (!workFields.getWsDateBefore().isEmpty()) {
      try {
        var dateBefore = LocalDate.parse(workFields.getWsDateBefore(), DATE_FORMATTER);
        workFields.setWsDateAfter(dateBefore);
      } catch (Exception e) {
        // If parsing fails, perform string copy as fallback
        workFields.setWsDateAfter(workFields.getWsDateBefore());
      }
    }
  }

  /**
   * Main method to run the Bipm012 program.
   *
   * @param args command line arguments (not used)
   */
  public static void main(String[] args) {
    var bipm012 = new Bipm012();
    bipm012.execute();
    
    System.out.println("BIPM012 program execution completed successfully.");
    System.out.println("Max Length: " + bipm012.getBipm012Parm().getOutputData().getMaxLength());
  }
}