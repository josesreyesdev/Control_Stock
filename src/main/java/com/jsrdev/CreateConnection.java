package com.jsrdev;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CreateConnection {

    // recuperar conexion
    public Connection retrieveConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost/control_stock?useTimeZone=true&serverTimeZone=UTC",
                "root",
                "Sarj950905"
        );
    }
}
