package com.ssss.util;


import com.ssss.exception.DatabaseUnavailableException;
import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@UtilityClass
public class ConnectionManager {
    private static final String URL = "jdbc:sqlite:C:\\Programming\\Java\\\\Currency-Exchanger\\src\\main\\resources\\database.db";
    private static final String DRIVER = "org.sqlite.JDBC";

    static {
        loadDriver();
    }

    private static void loadDriver() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            throw new DatabaseUnavailableException(e.getMessage());
        }
    }
}
