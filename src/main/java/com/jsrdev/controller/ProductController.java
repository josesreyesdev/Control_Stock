package com.jsrdev.controller;

import com.jsrdev.dao.ProductDAO;
import com.jsrdev.factory.ConnectionFactory;
import com.jsrdev.model.Product;

import java.util.List;

public class ProductController {

    ProductDAO productDao;

    public ProductController() {
        this.productDao = new ProductDAO(new ConnectionFactory().retrieveConnection());
    }

    public void save(Product product) {
        productDao.save(product);
    }

    public List<Product> list() {
        return productDao.list();
    }

    public int update(String name, String description, Integer quantity, Integer id) {
        return productDao.update(name, description, quantity, id);
    }

    public int delete(Integer id) {
        return productDao.delete(id);
    }
}
