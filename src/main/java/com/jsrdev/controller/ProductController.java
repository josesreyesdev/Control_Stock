package com.jsrdev.controller;

import com.jsrdev.factory.ConnectionFactory;
import com.jsrdev.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductController {

    public int modify(String name, String description, Integer quantity, Integer id) throws SQLException {

        final Connection connection = new ConnectionFactory().retrieveConnection();

        try (connection) {
            final PreparedStatement statement = connection.prepareStatement(
                    "UPDATE PRODUCTO SET NOMBRE = ?, DESCRIPCION = ?, CANTIDAD = ? WHERE ID = ?"
            );

            try (statement) {
                statement.setString(1, name);
                statement.setString(2, description);
                statement.setInt(3, quantity);
                statement.setInt(4, id);

                statement.execute();

                return statement.getUpdateCount();
            }
        }
    }

    public int delete(Integer id) throws SQLException {

        final Connection connection = new ConnectionFactory().retrieveConnection();
        try (connection) {
            final PreparedStatement statement = connection.prepareStatement("DELETE FROM PRODUCTO WHERE ID = ?");
            try (statement) {

                statement.setInt(1, id);
                statement.execute();

                return statement.getUpdateCount();
            }
        }
    }

    public void save(Product product) throws SQLException {

        final Connection connection = new ConnectionFactory().retrieveConnection();
        try (connection) {
            connection.setAutoCommit(false);

            final PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO PRODUCTO (nombre, descripcion, cantidad) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            try (statement) {
                executeRegister(product, statement);
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
            }
        }
    }

    private void executeRegister(Product product, PreparedStatement statement)
            throws SQLException {

        statement.setString(1, product.getName());
        statement.setString(2, product.getDescription());
        statement.setInt(3, product.getQuantity());

        statement.execute();

        final ResultSet resultSet = statement.getGeneratedKeys();

        try (resultSet) {
            while (resultSet.next()) {
                product.setId(resultSet.getInt(1));
                System.out.printf("Fue insertado el producto: %s ", product);
            }
        }
    }

    public List<Map<String, String>> list() throws SQLException {

        final Connection connection = new ConnectionFactory().retrieveConnection();

        try (connection) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT ID, NOMBRE, DESCRIPCION, CANTIDAD FROM PRODUCTO"
            );
            statement.execute();

            return getMapList(statement);
        }
    }

    private static List<Map<String, String>> getMapList(Statement statement) throws SQLException {
        final ResultSet resultSet = statement.getResultSet();
        try (resultSet) {
            List<Map<String, String>> result = new ArrayList<>();
            while (resultSet.next()) {  // para iterar hasta el ultimo elemento
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
}
