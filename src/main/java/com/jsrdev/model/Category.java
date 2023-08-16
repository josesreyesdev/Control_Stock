package com.jsrdev.model;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private final Integer id;
    private final String name;

    private List<Product> products;

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public Integer getId() {
        return id;
    }

    public void addProduct(Product product) {
        if (this.products == null) {
            this.products = new ArrayList<>();
        }

        this.products.add(product);
    }

    public List<Product> getProducts() {
        return this.products;
    }
}
