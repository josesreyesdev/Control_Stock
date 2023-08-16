package com.jsrdev.model;

public class Product {

    private Integer id;
    private final String name;
    private String description;
    private final Integer quantity;

    private Integer categoryId;

    public Product(String name, String description, Integer quantity) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
    }

    public Product(int id, String name, String description, int quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
    }

    public Product(Integer id, String name, Integer quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    @Override
    public String toString() {
        return String.format(
                "{Id: %d, Name: %s, Description: %s, Quantity: %d, CategoryId: %d}",
                this.id,
                this.name,
                this.description,
                this.quantity,
                this.categoryId
        );
    }
}
