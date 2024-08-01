package com.ssss.util;


import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@UtilityClass
public class ConnectionManager {
    private static final String URL_KEY = "db.url";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(PropertiesUtil.getProperty(URL_KEY));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
