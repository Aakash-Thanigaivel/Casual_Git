package com.bofa.alpha;

import java.util.ArrayList;
import java.util.List;

/**
 * Java equivalent of COBOL ALPHA program.
 * Program to exercise EXPECT statements converted to Java.
 * 
 * Following Google Java Style Guidelines as specified in P13456-Java - Coding Standard - Guide Lines-190625-095145.pdf
 */
public class Alpha {
    
    // Working storage section equivalent - instance variables
    private String wsField1;
    private String wsField2;
    private List<TableEntry1> wsTable1;
    private List<TableEntry2> wsTable2;
    private int wsDisplayNumeric;
    
    // Table entry classes following Google Java Style Guidelines
    public static class TableEntry1 {
        private String wsThing1;
        private String wsThing2;
        
        public TableEntry1() {
            this.wsThing1 = "";
            this.wsThing2 = "";
        }
        
        public String getWsThing1() {
            return wsThing1;
        }
        
        public void setWsThing1(String wsThing1) {
            this.wsThing1 = wsThing1 != null ? wsThing1 : "";
        }
        
        public String getWsThing2() {
            return wsThing2;
        }
        
        public void setWsThing2(String wsThing2) {
            this.wsThing2 = wsThing2 != null ? wsThing2 : "";
        }
    }
    
    public static class TableEntry2 {
        private String wsThing3;
        private String wsThing4;
        
        public TableEntry2() {
            this.wsThing3 = "";
            this.wsThing4 = "";
        }
        
        public String getWsThing3() {
            return wsThing3;
        }
        
        public void setWsThing3(String wsThing3) {
            this.wsThing3 = wsThing3 != null ? wsThing3 : "";
        }
        
        public String getWsThing4() {
            return wsThing4;
        }
        
        public void setWsThing4(String wsThing4) {
            this.wsThing4 = wsThing4 != null ? wsThing4 : "";
        }
    }
    
    /**
     * Constructor initializes all working storage fields.
     */
    public Alpha() {
        this.wsField1 = "";
        this.wsField2 = "";
        this.wsTable1 = new ArrayList<>(5);
        this.wsTable2 = new ArrayList<>(5);
        this.wsDisplayNumeric = 0;
        
        // Initialize tables with 5 entries each
        for (int i = 0; i < 5; i++) {
            this.wsTable1.add(new TableEntry1());
            this.wsTable2.add(new TableEntry2());
        }
    }
    
    /**
     * Main procedure division equivalent.
     * Executes the main program logic.
     */
    public void execute() {
        // GOBACK equivalent - method returns normally
        return;
    }
    
    /**
     * Main method for program execution.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        var alpha = new Alpha();
        alpha.execute();
    }
    
    // Getter and setter methods following Google Java Style Guidelines
    public String getWsField1() {
        return wsField1;
    }
    
    public void setWsField1(String wsField1) {
        this.wsField1 = wsField1 != null ? wsField1 : "";
    }
    
    public String getWsField2() {
        return wsField2;
    }
    
    public void setWsField2(String wsField2) {
        this.wsField2 = wsField2 != null ? wsField2 : "";
    }
    
    public List<TableEntry1> getWsTable1() {
        return new ArrayList<>(wsTable1);
    }
    
    public void setWsTable1(List<TableEntry1> wsTable1) {
        this.wsTable1 = wsTable1 != null ? new ArrayList<>(wsTable1) : new ArrayList<>(5);
    }
    
    public List<TableEntry2> getWsTable2() {
        return new ArrayList<>(wsTable2);
    }
    
    public void setWsTable2(List<TableEntry2> wsTable2) {
        this.wsTable2 = wsTable2 != null ? new ArrayList<>(wsTable2) : new ArrayList<>(5);
    }
    
    public int getWsDisplayNumeric() {
        return wsDisplayNumeric;
    }
    
    public void setWsDisplayNumeric(int wsDisplayNumeric) {
        this.wsDisplayNumeric = Math.max(0, Math.min(999, wsDisplayNumeric));
    }
}