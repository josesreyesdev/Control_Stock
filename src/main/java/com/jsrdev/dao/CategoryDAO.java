package com.jsrdev.dao;

import com.jsrdev.model.Category;
import com.jsrdev.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    private final Connection connection;
    public CategoryDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Category> list() {
        List<Category> result = new ArrayList<>();
        try {
            var querySelect = "SELECT ID, NOMBRE FROM CATEGORY";
            System.out.println(querySelect);
            final PreparedStatement statement = connection.prepareStatement(querySelect);

            try (statement) {
                final ResultSet resultSet = statement.executeQuery();
                try (resultSet) {
                    while (resultSet.next()) {
                        var category = new Category(
                                resultSet.getInt("ID"),
                                resultSet.getString("NOMBRE")
                        );
                        result.add(category);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    // Extrayendo los datos por inner join
    public List<Category> listWithProducts() {
        List<Category> result = new ArrayList<>();
        try {
            var querySelect = "SELECT C.ID, C.NOMBRE, P.ID, P.NOMBRE, P.CANTIDAD " +
                    "FROM CATEGORY C INNER JOIN PRODUCTO P ON C.ID = P.CATEGORY_ID";
            final PreparedStatement statement = connection.prepareStatement(querySelect);

            try (statement) {
                final ResultSet resultSet = statement.executeQuery();
                try (resultSet) {
                    while (resultSet.next()) {
                        int categoryId = resultSet.getInt("C.ID");
                        String categoryName = resultSet.getString("C.NOMBRE");

                        var category = result.stream()
                                .filter( cat -> cat.getId().equals(categoryId))
                                .findAny().orElseGet( () -> {
                                    Category cat=  new Category(categoryId, categoryName);
                                    result.add(cat);

                                    return cat;
                                });
                        var product = new Product(
                                resultSet.getInt("P.ID"),
                                resultSet.getString("P.NOMBRE"),
                                resultSet.getInt("P.CANTIDAD")
                        );

                        category.addProduct(product);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
