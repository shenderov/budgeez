package com.budgeez.common.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {

    private Connection databaseConnection;

    public DatabaseHelper (Connection databaseConnection){
        this.databaseConnection = databaseConnection;
    }

    public String getSingleResult (String sqlQuery) throws SQLException {
        String query = "SELECT * FROM currency WHERE currency_code='ILS'";
        Statement statement = databaseConnection.createStatement();
        System.out.println("statement: " + statement.executeQuery(query).getString(1));
        String res = statement.executeQuery(query).getNString(1);
        System.out.println(res);
        return null;
    }
}
