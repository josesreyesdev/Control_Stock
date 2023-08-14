package com.jsrdev.test;

import com.jsrdev.factory.ConnectionFactory;

import java.sql.SQLException;

public class TestPoolConnections {
    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        for (int i = 0; i < 20; i++) {
            try {
                connectionFactory.retrieveConnection();
                System.out.println("Abriendo la conexion de numero: " + (i + 1));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
