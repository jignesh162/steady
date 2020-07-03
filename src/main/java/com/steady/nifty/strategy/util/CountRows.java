package com.steady.nifty.strategy.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CountRows {
    private String indicesPath;
    private String tickPath;
    private Integer vixRows = 0;
    private Integer niftyRows = 0;
    //private Integer optionsRows = 0;
    private Integer tickRows = 0;
    private Integer wdayRows = 0;

    public static void main(String[] args) {
        /*CountRows countRows = new CountRows("C://Learn//OriginalData//INDICES//01JAN", "C://Learn//OriginalData//NIFTY_OPTIONS//01JAN");
        countRows.run();
        
        CountRows countRows1 = new CountRows("C://Learn//OriginalData//INDICES//02FEB", "C://Learn//OriginalData//NIFTY_OPTIONS//02FEB");
        countRows1.run();
        
        CountRows countRows2 = new CountRows("C://Learn//OriginalData//INDICES//03MAR", "C://Learn//OriginalData//NIFTY_OPTIONS//03MAR");
        countRows2.run();
        
        CountRows countRows3 = new CountRows("C://Learn//OriginalData//INDICES//04APR", "C://Learn//OriginalData//NIFTY_OPTIONS//04APR");
        countRows3.run();
        
        CountRows countRows4 = new CountRows("C://Learn//OriginalData//INDICES//05MAY", "C://Learn//OriginalData//NIFTY_OPTIONS//05MAY");
        countRows4.run();
       
        CountRows countRow5 = new CountRows("C://Learn//OriginalData//INDICES//06JUN", "C://Learn//OriginalData//NIFTY_OPTIONS//06JUN");
        countRow5.run(); */

        CountRows countRows = new CountRows("C://Learn//OriginalData//INDICES//07JUL", "C://Learn//OriginalData//NIFTY_OPTIONS//07JUL");
        countRows.run();
        
        CountRows countRows1 = new CountRows("C://Learn//OriginalData//INDICES//08AUG", "C://Learn//OriginalData//NIFTY_OPTIONS//08AUG");
        countRows1.run();
        
        CountRows countRows2 = new CountRows("C://Learn//OriginalData//INDICES//09SEP", "C://Learn//OriginalData//NIFTY_OPTIONS//09SEP");
        countRows2.run();
        
        CountRows countRows3 = new CountRows("C://Learn//OriginalData//INDICES//10OCT", "C://Learn//OriginalData//NIFTY_OPTIONS//10OCT");
        countRows3.run();
        
        CountRows countRows4 = new CountRows("C://Learn//OriginalData//INDICES//11NOV", "C://Learn//OriginalData//NIFTY_OPTIONS//11NOV");
        countRows4.run();
       
        CountRows countRow5 = new CountRows("C://Learn//OriginalData//INDICES//12DEC", "C://Learn//OriginalData//NIFTY_OPTIONS//12DEC");
        countRow5.run();
       
        ///////

        System.out.println("countRows : "+countRows.niftyRows);
        System.out.println("Vix rows : "+countRows.vixRows);
        System.out.println("Tick rows : "+countRows.tickRows);
        System.out.println("WDay rows : "+countRows.wdayRows);

        System.out.println("countRows1 : "+countRows1.niftyRows);
        System.out.println("Vix rows : "+countRows1.vixRows);
        System.out.println("Tick rows : "+countRows1.tickRows);
        System.out.println("WDay rows : "+countRows1.wdayRows);

        System.out.println("countRows2 : "+countRows2.niftyRows);
        System.out.println("Vix rows : "+countRows2.vixRows);
        System.out.println("Tick rows : "+countRows2.tickRows);
        System.out.println("WDay rows : "+countRows2.wdayRows);

        System.out.println("countRows3 : "+countRows3.niftyRows);
        System.out.println("Vix rows : "+countRows3.vixRows);
        System.out.println("Tick rows : "+countRows3.tickRows);
        System.out.println("WDay rows : "+countRows3.wdayRows);

        System.out.println("countRows4 : "+countRows4.niftyRows);
        System.out.println("Vix rows : "+countRows4.vixRows);
        System.out.println("Tick rows : "+countRows4.tickRows);
        System.out.println("WDay rows : "+countRows4.wdayRows);

        System.out.println("countRow5 : "+countRow5.niftyRows);
        System.out.println("Vix rows : "+countRow5.vixRows);
        System.out.println("Tick rows : "+countRow5.tickRows);
        System.out.println("WDay rows : "+countRow5.wdayRows);

        Integer total = countRows.tickRows+countRows1.tickRows+countRows2.tickRows+countRows3.tickRows+
        countRows4.tickRows+countRow5.tickRows;
        System.out.println("Total:: "+total);
    }

    public CountRows(String indicesPath, String tickPath) {
        this.indicesPath = indicesPath;
        this.tickPath = tickPath;
    }

    public void run() {
        try {
            execute(indicesPath, tickPath);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public void execute(String indicesPath, String tickPath) throws IOException {
        File indicesFolder = new File(indicesPath);
        if (indicesFolder.exists() && indicesFolder.isDirectory()) {
            //System.out.println("******** Data loading in progress for INDICES *********\n");
            goThroughIndicesFiles(indicesFolder.listFiles());
            //System.out.println("******** Data successfully loaded for INDICES *********\n");
        } else {
            System.out.println("ERROR: Couldn't find INDICES folder.\n");
        }

        // Load NIFTY OPTIONS tick data
        File niftyOptionsFolder = new File(tickPath);
        if (niftyOptionsFolder.exists() && niftyOptionsFolder.isDirectory()) {
           // System.out.println("******** Data loading in progress for NIFTY OPTIONS *********\n");
            goThroughNiftyOptionsFiles(niftyOptionsFolder.listFiles());
           // System.out.println("******** Data successfully loaded for NIFTY OPTIONS *********\n");
        } else {
            System.out.println("ERROR: Couldn't find NIFTY OPTIONS folder.\n");
        }
    }

    private void goThroughNiftyOptionsFiles(File[] listOfNiftyOptionsFiles) throws IOException {
        for (int i = 0; i < listOfNiftyOptionsFiles.length; i++) {
            if (listOfNiftyOptionsFiles[i].isFile()) {
                insertIntoNiftyOptions(listOfNiftyOptionsFiles[i]);
            } else if (listOfNiftyOptionsFiles[i].isDirectory()) {
                //System.out.println("Directory found inside NIFTY OPTIONS folder - " + listOfNiftyOptionsFiles[i].getName()+"\n");
                goThroughNiftyOptionsFiles(listOfNiftyOptionsFiles[i].listFiles());
            }
        }
    }

    private void insertIntoNiftyOptions(File file) throws IOException {
        try {
            insertIntoOptionsTick(file.getPath());
        } catch (NumberFormatException e) {
            System.out.println("ERROR: NF exception caught while inserting options name - " + file.getPath()
                    + " Exception - " + e.getMessage() + "\n");
        }
    }

    private void insertIntoOptionsTick(String filePath) throws IOException {
        try {
            BufferedReader lineReader = new BufferedReader(new FileReader(filePath));
            lineReader.readLine(); // skip header line

            Integer rowCount = 0;
            while (lineReader.readLine() != null) {
                rowCount++;
            }
            this.tickRows = this.tickRows + rowCount;
            lineReader.close();
            //System.out.println("Options tick file : " + filePath + " row count - "+rowCount+"\n");
        } catch (IOException e) {
            System.out.println("ERROR: IO exception caught while inserting options tick - " + filePath + " Exception - "
                    + e.getMessage() + "\n");
        }
    }

    private void goThroughIndicesFiles(File[] listOfIndicesFiles)
            throws IOException {
        for (int i = 0; i < listOfIndicesFiles.length; i++) {
            if (listOfIndicesFiles[i].isFile()) {
                if (listOfIndicesFiles[i].getName().startsWith("INDIA")) {
                    insertIntoIndices(listOfIndicesFiles[i].getPath(), "VIX");
                    //System.out.println("VIX file- " + listOfIndicesFiles[i].getPath() + "\n");
                } else if (listOfIndicesFiles[i].getName().startsWith("NIFTY")) {
                    insertIntoIndices(listOfIndicesFiles[i].getPath(), "NIFTY");
                    //System.out.println("Nifty file- " + listOfIndicesFiles[i].getPath() + "\n");
                } else {
                    System.out.println("ERROR: Incorrect file name - " + listOfIndicesFiles[i].getName() + "\n");
                }
            } else if (listOfIndicesFiles[i].isDirectory()) {
                //System.out.println("Directory found inside INDICES folder - " + listOfIndicesFiles[i].getName() + "\n");
                goThroughIndicesFiles(listOfIndicesFiles[i].listFiles());
            }
        }
    }

    private void insertIntoIndices(String filePath, String tableName) throws IOException {
        try {
            BufferedReader lineReader = new BufferedReader(new FileReader(filePath));
            lineReader.readLine(); // skip header line
            boolean insertWorkingDay = true;

            Integer rowCount = 0;
            while (lineReader.readLine() != null) {
                if ("NIFTY".equals(tableName) && insertWorkingDay) {
                    insertWorkingDay();
                    insertWorkingDay = false;
                }
                if("NIFTY".equals(tableName)) {
                    this.niftyRows++;
                } else if("VIX".equals(tableName)){
                    this.vixRows++;
                }else {
                    System.out.println("ERROR: Wrong table name.");
                }
            }

            if("NIFTY".equals(tableName)) {
                this.niftyRows = this.niftyRows + rowCount;
                //System.out.println("Nifty file : " + filePath + " row count - "+rowCount+"\n");
            } else if("VIX".equals(tableName)){
                this.vixRows = this.vixRows + rowCount;
               // System.out.println("Vix file : " + filePath + " row count - "+rowCount+"\n");
            }else {
                //System.out.println("ERROR: Wrong table name.");
            }
            lineReader.close();
        } catch (IOException e) {
            System.out.println("ERROR: IO exception while inserting indices data for the file - " + filePath+"\n");
        }
    }

    private void insertWorkingDay() throws IOException {
        this.wdayRows++;
    }
}