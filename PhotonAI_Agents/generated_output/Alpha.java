package com.bofa.alpha;

import java.util.Arrays;

/**
 * Java equivalent of the COBOL ALPHA program.
 *
 * <p>This class represents a converted COBOL program that was originally designed
 * to exercise EXPECT statements. The conversion preserves the original data structures
 * and program flow while using modern Java practices and JDK 17 features.
 */
public final class Alpha {

  /**
   * Represents a table entry with two string fields.
   * Equivalent to COBOL's ws-table-1-entry and ws-table-2-entry structures.
   */
  public static final class TableEntry {
    private String thing1;
    private String thing2;

    /**
     * Creates a new TableEntry with empty strings.
     */
    public TableEntry() {
      this.thing1 = "";
      this.thing2 = "";
    }

    /**
     * Creates a new TableEntry with specified values.
     *
     * @param thing1 the first field value (max 5 characters)
     * @param thing2 the second field value (max 5 characters)
     */
    public TableEntry(String thing1, String thing2) {
      this.thing1 = truncateToLength(thing1, 5);
      this.thing2 = truncateToLength(thing2, 5);
    }

    public String getThing1() {
      return thing1;
    }

    public void setThing1(String thing1) {
      this.thing1 = truncateToLength(thing1, 5);
    }

    public String getThing2() {
      return thing2;
    }

    public void setThing2(String thing2) {
      this.thing2 = truncateToLength(thing2, 5);
    }

    /**
     * Truncates a string to the specified maximum length.
     *
     * @param value the string to truncate
     * @param maxLength the maximum allowed length
     * @return the truncated string
     */
    private static String truncateToLength(String value, int maxLength) {
      if (value == null) {
        return "";
      }
      return value.length() > maxLength ? value.substring(0, maxLength) : value;
    }
  }

  /**
   * Working storage section equivalent - contains all program data.
   */
  public static final class WorkingStorage {
    // Equivalent to WS-FIELD-1 PIC X(80)
    private String wsField1;
    
    // Equivalent to ws-Field-2 PIC X(80)
    private String wsField2;
    
    // Equivalent to ws-table-1 with 5 entries
    private final TableEntry[] wsTable1;
    
    // Equivalent to ws-table-2 with 5 entries
    private final TableEntry[] wsTable2;
    
    // Equivalent to ws-display-numeric PIC 999
    private int wsDisplayNumeric;

    /**
     * Initializes the working storage with default values.
     */
    public WorkingStorage() {
      this.wsField1 = "";
      this.wsField2 = "";
      this.wsTable1 = new TableEntry[5];
      this.wsTable2 = new TableEntry[5];
      this.wsDisplayNumeric = 0;
      
      // Initialize table entries
      Arrays.setAll(wsTable1, i -> new TableEntry());
      Arrays.setAll(wsTable2, i -> new TableEntry());
    }

    // Getters and setters with COBOL field validation

    public String getWsField1() {
      return wsField1;
    }

    public void setWsField1(String wsField1) {
      this.wsField1 = truncateToLength(wsField1, 80);
    }

    public String getWsField2() {
      return wsField2;
    }

    public void setWsField2(String wsField2) {
      this.wsField2 = truncateToLength(wsField2, 80);
    }

    public TableEntry[] getWsTable1() {
      return Arrays.copyOf(wsTable1, wsTable1.length);
    }

    public TableEntry getWsTable1Entry(int index) {
      if (index < 0 || index >= wsTable1.length) {
        throw new IndexOutOfBoundsException("Table index out of bounds: " + index);
      }
      return wsTable1[index];
    }

    public void setWsTable1Entry(int index, TableEntry entry) {
      if (index < 0 || index >= wsTable1.length) {
        throw new IndexOutOfBoundsException("Table index out of bounds: " + index);
      }
      wsTable1[index] = entry;
    }

    public TableEntry[] getWsTable2() {
      return Arrays.copyOf(wsTable2, wsTable2.length);
    }

    public TableEntry getWsTable2Entry(int index) {
      if (index < 0 || index >= wsTable2.length) {
        throw new IndexOutOfBoundsException("Table index out of bounds: " + index);
      }
      return wsTable2[index];
    }

    public void setWsTable2Entry(int index, TableEntry entry) {
      if (index < 0 || index >= wsTable2.length) {
        throw new IndexOutOfBoundsException("Table index out of bounds: " + index);
      }
      wsTable2[index] = entry;
    }

    public int getWsDisplayNumeric() {
      return wsDisplayNumeric;
    }

    public void setWsDisplayNumeric(int wsDisplayNumeric) {
      // COBOL PIC 999 allows values 0-999
      if (wsDisplayNumeric < 0 || wsDisplayNumeric > 999) {
        throw new IllegalArgumentException("Display numeric value must be between 0 and 999");
      }
      this.wsDisplayNumeric = wsDisplayNumeric;
    }

    /**
     * Truncates a string to the specified maximum length.
     *
     * @param value the string to truncate
     * @param maxLength the maximum allowed length
     * @return the truncated string
     */
    private static String truncateToLength(String value, int maxLength) {
      if (value == null) {
        return "";
      }
      return value.length() > maxLength ? value.substring(0, maxLength) : value;
    }
  }

  private final WorkingStorage workingStorage;

  /**
   * Creates a new Alpha program instance.
   */
  public Alpha() {
    this.workingStorage = new WorkingStorage();
  }

  /**
   * Gets the working storage instance.
   *
   * @return the working storage containing all program data
   */
  public WorkingStorage getWorkingStorage() {
    return workingStorage;
  }

  /**
   * Executes the main program logic.
   * Equivalent to the PROCEDURE DIVISION in the original COBOL program.
   */
  public void execute() {
    // Original COBOL program only contained GOBACK statement
    // This method represents the main program execution point
    // Additional logic can be added here as needed
  }

  /**
   * Main method to run the Alpha program.
   *
   * @param args command line arguments (not used)
   */
  public static void main(String[] args) {
    var alpha = new Alpha();
    alpha.execute();
    
    // Program execution complete (equivalent to GOBACK)
    System.out.println("Alpha program execution completed successfully.");
  }
}