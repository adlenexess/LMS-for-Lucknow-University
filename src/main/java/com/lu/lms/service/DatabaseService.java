package com.lu.lms.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseService {
    private static final String DB_URL = "jdbc:h2:./lmsdb"; // Embedded in project dir
    private static final String USER = "sa";
    private static final String PASS = "";

    private static DatabaseService instance;
    private Connection connection;

    private DatabaseService() {
        initDB();
    }

    public static DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }

    private void initDB() {
        try {
            connection = DriverManager.getConnection(DB_URL + ";IFEXISTS=TRUE;DB_CLOSE_ON_EXIT=FALSE", USER, PASS);
            // Schema loaded via embedded RUNSCRIPT, ignore errors if exists
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static {
        try (Connection tempConn = DriverManager.getConnection(DB_URL + ";IFEXISTS=FALSE;INIT=RUNSCRIPT FROM 'classpath:schema.sql'", USER, PASS)) {
            // Force init schema once
        } catch (SQLException e) {
            // Ignore
        }
    }


public Connection getConnection() {
        if (connection == null || isClosed(connection)) {
            try {
                connection = DriverManager.getConnection(DB_URL + ";IFEXISTS=TRUE;DB_CLOSE_ON_EXIT=FALSE", USER, PASS);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
    
    private boolean isClosed(Connection conn) {
        try {
            return conn == null || conn.isClosed();
        } catch (SQLException e) {
            return true;
        }
    }

}
