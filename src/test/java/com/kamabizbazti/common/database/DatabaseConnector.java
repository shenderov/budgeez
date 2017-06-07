package com.kamabizbazti.common.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnector {

    private static DatabaseConnector dbc = null;
    private Connection connection;

    private DatabaseConnector(String driverName, String databaseUrl, String username, String password) throws Exception{
        Class.forName(driverName);
        connection = DriverManager.getConnection(databaseUrl, username, password);
    }

    public static Connection getDatabaseConnection(String driverName, String databaseUrl, String username, String password) throws Exception{
        if (dbc == null){
            dbc = new DatabaseConnector(driverName, databaseUrl, username, password);
        }
        return dbc.connection;
    }
}
