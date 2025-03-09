package com.utilities;

import java.sql.*;
import java.util.*;

public class MySqlUtility {

    private Connection con = null;
    private String databaseName;
    private String databaseServer;
    private static final int QUERY_TIMEOUT = 30; // Set timeout in seconds


    public MySqlUtility(String databaseName) {
        this.databaseName = databaseName;
        if (ConfigReader.getValue("DbEnv").equalsIgnoreCase("Qa")) {
            this.databaseServer = ConfigReader.getValue("HostName");
        } else {
            this.databaseServer = ConfigReader.getValue("HostName");
        }
    }

    public MySqlUtility(String databaseName, String databaseServer) {
        this.databaseName = databaseName;
        this.databaseServer = databaseServer;
    }




    public Connection getSQLConnection() {
        try {
            String url;
            String user;
            String password;

            if (ConfigReader.getValue("DbEnv").equalsIgnoreCase("Qa")) {
                url = "jdbc:mysql://" + databaseServer + "/" + ConfigReader.getValue("DatabaseName") +"?useSSL=false&serverTimezone=UTC";
                user = ConfigReader.getValue("Database_UserName");
                password = ConfigReader.getValue("Database_Password");
            } else {
                url = "jdbc:mysql://" + databaseServer + "/" + ConfigReader.getValue("DatabaseName") +"?useSSL=false&serverTimezone=UTC";
                user = ConfigReader.getValue("ProdDatabase_UserName");
                password = ConfigReader.getValue("ProdDatabase_Password");
            }

            // Establish MySQL connection
            con = DriverManager.getConnection(url, user, password);
            System.out.println("MySQL Database connected successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }
    public ResultSet executeQuery(Connection connection, String query) throws SQLException {
        if (connection == null || query == null || query.isEmpty()) {
            throw new SQLException("Invalid database connection or empty query.");
        }

        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = connection.createStatement();
            stmt.setQueryTimeout(QUERY_TIMEOUT);

            boolean hasResults = stmt.execute(query);

            if (!hasResults) {
                System.out.println("No results returned.");
                return null;
            } else {
                rs = stmt.getResultSet();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return rs;
    }
    public List<Map<String, Object>> convertMulitpleDBResultToHashMap(ResultSet resultSet) throws SQLException {
        List<Map<String, Object>> dbResultList = new ArrayList<Map<String, Object>>();
        ResultSetMetaData md = resultSet.getMetaData();


        int columns = md.getColumnCount();

        while (resultSet.next()) {
            Map<String, Object> resultValues = new HashMap<String, Object>();
            for (int i = 1; i <= columns; i++) {

                resultValues.put(md.getColumnName(i), resultSet.getObject(i));
                System.out.println(resultSet.getObject(i));
            }

            dbResultList.add(resultValues);

        }


        return dbResultList;
    }

    public static void insertCountries(Connection conn, List<String> countries,String columnName) {
        String sql = "INSERT INTO europe (columnName) VALUES (?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (String country : countries) {
                pstmt.setString(1, country);
                pstmt.addBatch(); // Add to batch
            }

            int[] rowsInserted = pstmt.executeBatch(); // Execute batch insert
            System.out.println("Successfully inserted " + rowsInserted.length + " countries into the database!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void insertCountriesNumber(Connection conn, List<Long> countries, String columnName) {
        String sql = "INSERT INTO europe (" + columnName + ") VALUES (?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (Long country : countries) {
                pstmt.setLong(1, country);  // Use setLong for numeric values
                pstmt.addBatch();  // Add to batch
            }

            int[] rowsInserted = pstmt.executeBatch();  // Execute batch insert
            System.out.println("Successfully inserted " + rowsInserted.length + " rows into the database!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertValuesInDB(Connection sqlConnection, List<String> countriesListDB,
                                        List<Long> columnValues, String name) {

        if (countriesListDB.size() != columnValues.size()) {
            System.out.println("Mismatch between database countries and column values.");
            return;
        }

        String updateSQL = "UPDATE europe SET "+name+" = ? WHERE country_name = ?";

        try (PreparedStatement pstmt = sqlConnection.prepareStatement(updateSQL)) {
            for (int i = 0; i < columnValues.size(); i++) {
                pstmt.setString(1, String.valueOf(columnValues.get(i)));  // `totalCases` is a single string
                pstmt.setString(2, countriesListDB.get(i));  // Update using countriesListDB
                pstmt.addBatch();
            }

            int[] updateCounts = pstmt.executeBatch();
            System.out.println("Data updated successfully! Rows affected: " + updateCounts.length);

        } catch (SQLException e) {
            System.err.println("SQL Exception while updating database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static ArrayList<String> processDatabaseMap(List<Map<String, Object>> finTeacherTestDataMap, String columnName)  {
        Map<String, String> columnValues = getDBColumnValues(finTeacherTestDataMap, columnName);
        ArrayList<String> uniqueValues = new ArrayList<>(columnValues.values());
        return uniqueValues;
    }
    public static Map<String, String> getDBColumnValues(List<Map<String, Object>> finTeacherTestDataMap, String value) {
        Map<String, String> findTeacherTestDataMap =  new LinkedHashMap<>();
        for (Map<String, Object> s : finTeacherTestDataMap) {
            String subject= generateRandomString();

            findTeacherTestDataMap.put(subject, String.valueOf(s.get(value)));
        }

        return findTeacherTestDataMap;
    }

    public static String generateRandomString() {
        long currentTime = System.currentTimeMillis() ; // Convert milliseconds to seconds
        Random random = new Random();
        int randomOffset = random.nextInt(1000000); // Generate a random offset between 0 and 999

        long timestampWithRandom = currentTime + randomOffset;
        String value="Automation_"+timestampWithRandom;
        return value;
    }


}
