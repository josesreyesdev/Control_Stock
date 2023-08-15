package com.jsrdev.dao;

import com.jsrdev.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private final Connection connection;

    public ProductDAO( Connection connection) {
        this.connection = connection;
    }

    public void save(Product product) {
        try {
            //connection.setAutoCommit(false); // Sirve para garantizar y coherencia en una transaccion en la bd

            final PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO PRODUCTO (nombre, descripcion, cantidad) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            try (statement) {
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

                /**
                 * connection.commit(); // Sirve para confirmar todas las op como transaccion unica
                 * connection.rollback(); // Descartar op realizas en una transaccion si hubo un error en el catch
                 * */
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // QUERY OR SELECT
    public List<Product> list() {
        List<Product> result = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT ID, NOMBRE, DESCRIPCION, CANTIDAD FROM PRODUCTO"
            );

            try (statement) {
                statement.execute();

                final ResultSet resultSet = statement.getResultSet();

                try (resultSet) {
                    while (resultSet.next()) {  // para iterar hasta el ultimo elemento
                        Product rowProduct = new Product(
                                resultSet.getInt("ID"),
                                resultSet.getString("NOMBRE"),
                                resultSet.getString("DESCRIPCION"),
                                resultSet.getInt("CANTIDAD")
                        );

                        result.add(rowProduct);
                    }
                }
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // UPDATE
    public int update(String name, String description, Integer quantity, Integer id) {

        try {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // DELETE
    public int delete(Integer id) {
        try {

            final PreparedStatement statement = connection.prepareStatement("DELETE FROM PRODUCTO WHERE ID = ?");

            try (statement) {
                statement.setInt(1, id);
                statement.execute();

                return statement.getUpdateCount();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
