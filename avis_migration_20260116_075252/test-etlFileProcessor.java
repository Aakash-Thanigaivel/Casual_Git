// File processing utilities
package com.batch.etl;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileProcessor {

    public static List<String> readAllLines(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }

    public static void writeLine(BufferedWriter writer, String line) throws IOException {
        writer.write(line);
        writer.newLine();
    }

    public static void writeRecord(BufferedWriter writer, String record) throws IOException {
        writer.write(record);
    }
    
    public static void writeRecord(BufferedWriter writer, String record, boolean newLine) throws IOException {
        writer.write(record);
        if (newLine) {
            writer.newLine();
        }
    }
}
