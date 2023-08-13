package com.jsrdev.controller;

import com.jsrdev.factory.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductController {

	public int modify(String name, String description, Integer quantity, Integer id) throws SQLException {

        Connection connection = new ConnectionFactory().retrieveConnection();
        PreparedStatement statement =  connection.prepareStatement(
                "UPDATE PRODUCTO SET NOMBRE = ?, DESCRIPCION = ?, CANTIDAD = ? WHERE ID = ?"
        );
        statement.setString(1, name);
        statement.setString(2, description);
        statement.setInt(3, quantity);
        statement.setInt(4, id);

        statement.execute();

        int updateCount = statement.getUpdateCount();
        connection.close();

        return updateCount;
    }

	public int delete(Integer id) throws SQLException {

        Connection connection = new ConnectionFactory().retrieveConnection();
        PreparedStatement statement = connection.prepareStatement("DELETE FROM PRODUCTO WHERE ID = ?");
        statement.setInt(1, id);
        statement.execute();

        int deleteCount = statement.getUpdateCount();
        connection.close();

        return deleteCount;
	}

	public void save(Map<String, String> product) throws SQLException {
        String name = product.get("NOMBRE");
        String description = product.get("DESCRIPCION");
        int quantity = Integer.parseInt(product.get("CANTIDAD"));
        int maxQuantity = 50;

        Connection connection = new ConnectionFactory().retrieveConnection();
        connection.setAutoCommit(false);

        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO PRODUCTO (nombre, descripcion, cantidad) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        );

        try {
            do {
                int quantityToSave = Math.min(quantity, maxQuantity);
                executeRegister(name, description, quantityToSave, statement);
                quantity -= maxQuantity;
            } while ( quantity > 0);

            connection.commit();
        } catch (Exception e) {
            connection.rollback();
        }
        statement.close();
        connection.close();
	}

    private void executeRegister(String name, String description, Integer quantity, PreparedStatement statement)
            throws SQLException {

        statement.setString(1, name);
        statement.setString(2, description);
        statement.setInt(3, quantity);

        statement.execute( );

        ResultSet resultSet = statement.getGeneratedKeys();
        while(resultSet.next()) {
            int generatedId = resultSet.getInt(1);
            System.out.printf("Fue insertado el producto con Id: %d", generatedId);
        }
    }

    public List<Map<String, String>> list() throws SQLException {
        Connection connection = new ConnectionFactory().retrieveConnection();

        PreparedStatement statement = connection.prepareStatement(
                "SELECT ID, NOMBRE, DESCRIPCION, CANTIDAD FROM PRODUCTO"
        );
        statement.execute();

        List<Map<String, String>> result = getMapList(statement);

        connection.close();

        return result;
    }

    private static List<Map<String, String>> getMapList(Statement statement) throws SQLException {
        //Nos devuelve un obj de tipo resultSet
        ResultSet resultSet = statement.getResultSet();

        List<Map<String, String>> result = new ArrayList<>();
        while (resultSet.next() ) {  // para iterar hasta el ultimo elemento
            Map<String, String> fila = new HashMap<>();
            fila.put("ID", String.valueOf(resultSet.getInt("ID")));
            fila.put("NOMBRE", resultSet.getString("NOMBRE"));
            fila.put("DESCRIPCION", resultSet.getString("DESCRIPCION"));
            fila.put("CANTIDAD", String.valueOf(resultSet.getInt("CANTIDAD")));

            result.add(fila);
        }
        return result;
    }
}
