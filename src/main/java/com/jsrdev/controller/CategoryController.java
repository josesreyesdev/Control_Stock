package com.jsrdev.controller;

import com.jsrdev.dao.CategoryDAO;
import com.jsrdev.factory.ConnectionFactory;
import com.jsrdev.model.Category;

import java.util.List;

public class CategoryController {

    private final CategoryDAO categoryDao;

    public CategoryController() {
        var factory = new ConnectionFactory();
        this.categoryDao = new CategoryDAO(factory.retrieveConnection());
    }

	public List<Category> list() {
		return categoryDao.list();
	}

    public List<Category> reportUpload() { //cargar reporte
        return this.categoryDao.listWithProducts();
    }

}
