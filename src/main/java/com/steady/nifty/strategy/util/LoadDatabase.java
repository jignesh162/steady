package com.steady.nifty.strategy.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoadDatabase extends Thread {
    private static final int BATCH_SIZE = 20;
    Connection connection = null;
    Statement statement = null;
    private String indicesPath;
    private String tickPath;

    public LoadDatabase(String indicesPath, String tickPath) {
        this.indicesPath = indicesPath;
        this.tickPath = tickPath;
    }

    public void run() {
        try {
            execute(indicesPath, tickPath);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void execute(String indicesPath, String tickPath) throws IOException, SQLException {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/steadydb?currentSchema=steady",
                    "postgres", "postgres");
            statement = connection.createStatement();
            System.out.println("Database opened for path "+tickPath+"\n");
            // Load INDICES tick data
            File indicesFolder = new File(indicesPath);
            if (indicesFolder.exists() && indicesFolder.isDirectory()) {
                System.out.println("** Data loading in progress for INDICES -> " + indicesPath +"\n");
                goThroughIndicesFiles(connection, statement, indicesFolder.listFiles());
                System.out.println("** Data successfully loaded for INDICES -> " + indicesPath +"\n");
            } else {
                System.out.println("ERROR: Couldn't find INDICES folder.\n");
            }

            // Load NIFTY OPTIONS tick data
            File niftyOptionsFolder = new File(tickPath);
            if (niftyOptionsFolder.exists() && niftyOptionsFolder.isDirectory()) {
                System.out.println("** Data loading in progress for NIFTY OPTIONS -> " + tickPath +"\n");
                goThroughNiftyOptionsFiles(connection, statement, niftyOptionsFolder.listFiles());
                System.out.println("** Data successfully loaded for NIFTY OPTIONS -> " + tickPath +"\n");
            } else {
                System.out.println("ERROR: Couldn't find NIFTY OPTIONS folder.\n");
            }

            statement.close();
            connection.close();
            System.out.println("Database closed for path "+tickPath+"\n");
        } catch (SQLException e) {
            System.out.println("ERROR: Couldn't connect to DB with exception - " + e.getMessage() + "\n");
            throw e;
        }
    }

    private void goThroughNiftyOptionsFiles(Connection conn, Statement stmt, File[] listOfNiftyOptionsFiles) throws IOException {
        for (int i = 0; i < listOfNiftyOptionsFiles.length; i++) {
            if (listOfNiftyOptionsFiles[i].isFile()) {
                insertIntoNiftyOptions(conn, stmt, listOfNiftyOptionsFiles[i]);
            } else if (listOfNiftyOptionsFiles[i].isDirectory()) {
                goThroughNiftyOptionsFiles(conn, stmt, listOfNiftyOptionsFiles[i].listFiles());
            }
        }
    }

    private void insertIntoNiftyOptions(Connection conn, Statement stmt, File file) throws IOException {
        try {
            OptionsData optionData = OptionReader.readTickerInformation(file.getPath());
            ResultSet getQueryRS = getOptionDetails(conn, optionData);
            if (getQueryRS.next() == false) {
                ResultSet insertQueryRS = insertOptionDetails(conn, optionData);
                while (insertQueryRS.next()) {
                    insertIntoOptionsTick(conn, stmt, file.getPath(), insertQueryRS.getInt(1));
                }
            } else {
                Integer insertedOptionsDetailsId = getQueryRS.getObject(1, Integer.class);
                insertIntoOptionsTick(conn, stmt, file.getPath(), insertedOptionsDetailsId);
            }
        } catch (NumberFormatException e) {
            System.out.println("ERROR: NF exception caught while inserting options name - " + file.getPath()
                    + " Exception - " + e.getMessage() + "\n");
        } catch (SQLException e) {
            System.out.println("ERROR: SQL exception caught while inserting options name - " + file.getPath()
                    + " Exception - " + e.getMessage() + "\n");
        }
    }

    private ResultSet insertOptionDetails(Connection conn, OptionsData optionData) throws SQLException {
        String insertOptionsNameSql = "INSERT INTO OPTIONS_DETAILS ( NAME, EXPIRY_DATE, STRIKE, OPTION ) values (?,?,?,?)";
        PreparedStatement optionDetailsInsert = conn.prepareStatement(insertOptionsNameSql,
                Statement.RETURN_GENERATED_KEYS);
        optionDetailsInsert.setString(1, optionData.getName());
        optionDetailsInsert.setDate(2, optionData.getExpiryDate());
        optionDetailsInsert.setInt(3, optionData.getStrike());
        optionDetailsInsert.setString(4, optionData.getOption());
        optionDetailsInsert.executeUpdate();

        ResultSet optionDetail = optionDetailsInsert.getGeneratedKeys();
        return optionDetail;
    }

    private ResultSet getOptionDetails(Connection conn, OptionsData optionData) throws SQLException {
        String optionDetailsGetSql = "SELECT ID from OPTIONS_DETAILS WHERE NAME = ? AND EXPIRY_DATE = ? AND STRIKE = ? AND OPTION = ?";
        PreparedStatement optionDetailsGet = conn.prepareStatement(optionDetailsGetSql);
        optionDetailsGet.setString(1, optionData.getName());
        optionDetailsGet.setDate(2, optionData.getExpiryDate());
        optionDetailsGet.setInt(3, optionData.getStrike());
        optionDetailsGet.setString(4, optionData.getOption());
        ResultSet optionDetails = optionDetailsGet.executeQuery();
        return optionDetails;
    }

    private void insertIntoOptionsTick(Connection connection, Statement stmt, String filePath,
            Integer insertedOptionsNameId) throws IOException {
        try {
            
            String sql = "INSERT INTO OPTIONS_TICK (DATE_TIME, LTP, BUYPRICE, BUYQTY, SELLPRICE, SELLQTY, LTQ, OPENINTEREST, OPTIONS_ID) VALUES (?,?,?,?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            BufferedReader lineReader = new BufferedReader(new FileReader(filePath));
            String lineText = null;
            int count = 0;
            lineReader.readLine(); // skip header line

            while ((lineText = lineReader.readLine()) != null) {
                TickData tickData = new TickData(lineText);
                statement.setTimestamp(1, tickData.getDateTimestamp());
                statement.setBigDecimal(2, tickData.getLtp());
                statement.setBigDecimal(3, tickData.getBuyPrice());
                statement.setInt(4, tickData.getBuyQty());
                statement.setBigDecimal(5, tickData.getSellPrice());
                statement.setInt(6, tickData.getSellQty());
                statement.setInt(7, tickData.getLtq());
                statement.setInt(8, tickData.getOpenInterest());
                statement.setFloat(9, insertedOptionsNameId);

                statement.addBatch();
                if (count % BATCH_SIZE == 0) {
                    statement.executeBatch();
                }
            }
            lineReader.close();
            statement.executeBatch();
            
        } catch (IOException e) {
            System.out.println("ERROR: IO exception caught while inserting options tick - " + filePath + " Exception - "
                    + e.getMessage() + "\n");
        } catch (SQLException e) {
            System.out.println("ERROR: SQL exception caught while inserting options tick - " + filePath
                    + " Exception - " + e.getMessage() + "\n");
        }
    }

    private void goThroughIndicesFiles(Connection connection, Statement stmt, File[] listOfIndicesFiles)
            throws IOException {
        for (int i = 0; i < listOfIndicesFiles.length; i++) {
            if (listOfIndicesFiles[i].isFile()) {
                if (listOfIndicesFiles[i].getName().startsWith("INDIA")) {
                   
                    insertIntoIndices(connection, stmt, listOfIndicesFiles[i].getPath(), "VIX");
                   
                } else if (listOfIndicesFiles[i].getName().startsWith("NIFTY")) {
                    
                    insertIntoIndices(connection, stmt, listOfIndicesFiles[i].getPath(), "NIFTY");
                   
                } else {
                    System.out.println("ERROR: Incorrect file name - " + listOfIndicesFiles[i].getName() + "\n");
                }
            } else if (listOfIndicesFiles[i].isDirectory()) {
                goThroughIndicesFiles(connection, stmt, listOfIndicesFiles[i].listFiles());
            }
        }
    }

    private void insertIntoIndices(Connection connection, Statement stmt, String filePath, String tableName) throws IOException {
        try {
            String sql = "INSERT INTO " + tableName + " (DATE_TIME, VALUE) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            BufferedReader lineReader = new BufferedReader(new FileReader(filePath));
            String lineText = null;
            int count = 0;
            lineReader.readLine(); // skip header line
            boolean insertWorkingDay = true;

            while ((lineText = lineReader.readLine()) != null) {
                TickData tickData = new TickData(lineText);
                statement.setTimestamp(1, tickData.getDateTimestamp());
                statement.setBigDecimal(2, tickData.getLtp());
                if ("NIFTY".equals(tableName) && insertWorkingDay) {
                    insertWorkingDay(stmt, tickData.getDate());
                    insertWorkingDay = false;
                }
                statement.addBatch();
                if (count % BATCH_SIZE == 0) {
                    statement.executeBatch();
                }
            }

            lineReader.close();
            statement.executeBatch();
        } catch (IOException | SQLException e) {
            System.out.println("ERROR: IO exception while inserting indices data for the file - " + filePath+"\n");
        }
    }

    private void insertWorkingDay(Statement stmt, String date) throws IOException {
        String sql = "INSERT INTO steady.WORKING_DAY (DAY) VALUES ('" + date + "')";
        try {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("ERROR: SQL exception while adding working day - " + date + "\n");
        }
    }
}