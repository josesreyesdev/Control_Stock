package com.jsrdev.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public Connection retrieveConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost/control_stock?useTimeZone=true&serverTimeZone=UTC",
                "root",
                "123456"
        );
    }
}
