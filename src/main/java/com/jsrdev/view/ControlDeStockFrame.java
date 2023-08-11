package com.jsrdev.view;

import com.jsrdev.controller.CategoryController;
import com.jsrdev.controller.ProductController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Optional;

public class ControlDeStockFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JLabel labelName, labelDescription, labelQuantity, labelCategory;
    private JTextField textName, textDescription, textQuantity;
    private JComboBox<Object> comboCategory;
    private JButton saveButton, modifyButton, clearButton, deleteButton, reportButton;
    private JTable table;
    private DefaultTableModel model;
    private ProductController productController;
    private CategoryController categoryController;

    public ControlDeStockFrame() {
        super("Products");

        this.categoryController = new CategoryController();
        this.productController = new ProductController();

        Container container = getContentPane();
        setLayout(null);

        configureFormFields(container);

        configTableContent(container);

        configFormActions();
    }

    private void configTableContent(Container container) {
        table = new JTable();

        model = (DefaultTableModel) table.getModel();
        model.addColumn("Id Producto");
        model.addColumn("Nombre Producto");
        model.addColumn("Descripción Producto");
        model.addColumn("Cantidad Producto");

        cargarTabla();

        table.setBounds(10, 205, 760, 280);

        deleteButton = new JButton("Eliminar");
        modifyButton = new JButton("Modificar");
        reportButton = new JButton("Ver Reporte");
        deleteButton.setBounds(10, 500, 80, 20);
        modifyButton.setBounds(100, 500, 80, 20);
        reportButton.setBounds(190, 500, 80, 20);

        container.add(table);
        container.add(deleteButton);
        container.add(modifyButton);
        container.add(reportButton);

        setSize(800, 600);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void configureFormFields(Container container) { //confi campos del formulario
        labelName = new JLabel("Nombre del Producto");
        labelDescription = new JLabel("Descripción del Producto");
        labelQuantity = new JLabel("Cantidad");
        labelCategory = new JLabel("Categoría del Producto");

        labelName.setBounds(10, 10, 240, 15);
        labelDescription.setBounds(10, 50, 240, 15);
        labelQuantity.setBounds(10, 90, 240, 15);
        labelCategory.setBounds(10, 130, 240, 15);

        labelName.setForeground(Color.BLACK);
        labelDescription.setForeground(Color.BLACK);
        labelCategory.setForeground(Color.BLACK);

        textName = new JTextField();
        textDescription = new JTextField();
        textQuantity = new JTextField();
        comboCategory = new JComboBox<>();
        comboCategory.addItem("Elige una Categoría");

        // TODO
        var categorias = this.categoryController.listar();
        // categorias.forEach(categoria -> comboCategoria.addItem(categoria));

        textName.setBounds(10, 25, 265, 20);
        textDescription.setBounds(10, 65, 265, 20);
        textQuantity.setBounds(10, 105, 265, 20);
        comboCategory.setBounds(10, 145, 265, 20);

        saveButton = new JButton("Guardar");
        clearButton = new JButton("Limpiar");
        saveButton.setBounds(10, 175, 80, 20);
        clearButton.setBounds(100, 175, 80, 20);

        container.add(labelName);
        container.add(labelDescription);
        container.add(labelQuantity);
        container.add(labelCategory);
        container.add(textName);
        container.add(textDescription);
        container.add(textQuantity);
        container.add(comboCategory);
        container.add(saveButton);
        container.add(clearButton);
    }

    private void configFormActions() { // Config acciones del formulario
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guardar();
                limpiarTabla();
                cargarTabla();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminar();
                limpiarTabla();
                cargarTabla();
            }
        });

        modifyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modificar();
                limpiarTabla();
                cargarTabla();
            }
        });

        reportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abrirReporte();
            }
        });
    }

    private void abrirReporte() {
        new ReporteFrame(this);
    }

    private void limpiarTabla() {
        model.getDataVector().clear();
    }

    private boolean tieneFilaElegida() {
        return table.getSelectedRowCount() == 0 || table.getSelectedColumnCount() == 0;
    }

    private void modificar() {
        if (tieneFilaElegida()) {
            JOptionPane.showMessageDialog(this, "Por favor, elije un item");
            return;
        }

        Optional.ofNullable(model.getValueAt(table.getSelectedRow(), table.getSelectedColumn()))
                .ifPresentOrElse(fila -> {
                    Integer id = (Integer) model.getValueAt(table.getSelectedRow(), 0);
                    String nombre = (String) model.getValueAt(table.getSelectedRow(), 1);
                    String descripcion = (String) model.getValueAt(table.getSelectedRow(), 2);

                    this.productController.modificar(nombre, descripcion, id);
                }, () -> JOptionPane.showMessageDialog(this, "Por favor, elije un item"));
    }

    private void eliminar() {
        if (tieneFilaElegida()) {
            JOptionPane.showMessageDialog(this, "Por favor, elije un item");
            return;
        }

        Optional.ofNullable(model.getValueAt(table.getSelectedRow(), table.getSelectedColumn()))
                .ifPresentOrElse(fila -> {
                    Integer id = (Integer) model.getValueAt(table.getSelectedRow(), 0);

                    this.productController.eliminar(id);

                    model.removeRow(table.getSelectedRow());

                    JOptionPane.showMessageDialog(this, "Item eliminado con éxito!");
                }, () -> JOptionPane.showMessageDialog(this, "Por favor, elije un item"));
    }

    private void cargarTabla() {
        try {
            var products = this.productController.listar();
            products.forEach(product -> model.addRow(new Object[] {
                    product.get("ID"), product.get("NOMBRE"), product.get("DESCRIPCION"), product.get("CANTIDAD") }));
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    private void guardar() {
        if (textName.getText().isBlank() || textDescription.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Los campos Nombre y Descripción son requeridos.");
            return;
        }

        Integer cantidadInt;

        try {
            cantidadInt = Integer.parseInt(textQuantity.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, String
                    .format("El campo cantidad debe ser numérico dentro del rango %d y %d.", 0, Integer.MAX_VALUE));
            return;
        }

        var producto = new Object[] { textName.getText(), textDescription.getText(), cantidadInt };
        var categoria = comboCategory.getSelectedItem();

        this.productController.guardar(producto);

        JOptionPane.showMessageDialog(this, "Registrado con éxito!");

        this.clearForm();
    }

    private void clearForm() {
        this.textName.setText("");
        this.textDescription.setText("");
        this.textQuantity.setText("");
        this.comboCategory.setSelectedIndex(0);
    }

}
