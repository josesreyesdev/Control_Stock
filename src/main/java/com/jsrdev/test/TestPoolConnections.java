package com.jsrdev.test;

import com.jsrdev.factory.ConnectionFactory;

public class TestPoolConnections {
    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        for (int i = 0; i < 20; i++) {
            connectionFactory.retrieveConnection();
            System.out.println("Abriendo la conexion de numero: " + (i + 1));
        }
    }
}
