package com.steady.nifty.strategy.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class OptionReader {
    public static OptionsData readTickerInformation(String filePath) throws IOException {
        try {
            BufferedReader lineReader = new BufferedReader(new FileReader(filePath));
            lineReader.readLine(); // skip header line
            String firstLine = lineReader.readLine(); // First line
            lineReader.close();
            String[] data = firstLine.split(","); // Split by comma
            return new OptionsData(data[0], filePath);
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: File not found while reading ticker data - " + filePath+"\n");
            return null;
        } catch (IOException e) {
            System.out.println("ERROR while reading ticker data - " + filePath+"\n");
            return null;
        }
    }
}