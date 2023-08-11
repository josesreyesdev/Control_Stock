package com.jsrdev.controller;

import com.jsrdev.CreateConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductController {

	public void modificar(String nombre, String descripcion, Integer id) {
		// TODO
	}

	public void eliminar(Integer id) {
		// TODO
	}

	public List<Map<String, String>> listar() throws SQLException {
		Connection connection = new CreateConnection().retrieveConnection();

		//Statement nos devuelve un objeto del mismo tipo, para hacer nuestra consulta
		Statement statement = connection.createStatement();
		statement.execute("SELECT ID, NOMBRE, DESCRIPCION, CANTIDAD FROM PRODUCTO");

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

	public void guardar(Object producto) {
		// TODO
	}

}
