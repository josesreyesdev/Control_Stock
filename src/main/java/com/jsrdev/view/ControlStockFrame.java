package com.jsrdev.view;

import com.jsrdev.controller.CategoryController;
import com.jsrdev.controller.ProductController;
import com.jsrdev.model.Category;
import com.jsrdev.model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.Serial;
import java.util.Objects;
import java.util.Optional;

public class ControlStockFrame extends JFrame {

    @Serial
    private static final long serialVersionUID = 1L;

    private JLabel labelName, labelDescription, labelQuantity, labelCategory;
    private JTextField textName, textDescription, textQuantity;
    private JComboBox<Category> comboCategory;
    private JButton saveButton, modifyButton, clearButton, deleteButton, reportButton;
    private JTable table;
    private DefaultTableModel model;
    private final ProductController productController;
    private final CategoryController categoryController;

    public ControlStockFrame() {
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

        loadTable();

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
        comboCategory.addItem(new Category(0, "Elige una Categoría"));

        var categories = this.categoryController.list();
        categories.forEach(category -> comboCategory.addItem(category));

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
        saveButton.addActionListener(e -> {
            save();
            clearTable();
            loadTable();
        });

        clearButton.addActionListener(e -> clearForm());

        deleteButton.addActionListener(e -> {
            delete();
            clearTable();
            loadTable();
        });

        modifyButton.addActionListener(e -> {
            update();
            clearTable();
            loadTable();
        });

        reportButton.addActionListener(e -> openReport());
    }

    private void openReport() {
        new ReportFrame(this);
    }

    private void clearTable() {
        model.getDataVector().clear();
    }

    private boolean hasSelectedRow() {
        return table.getSelectedRowCount() == 0 || table.getSelectedColumnCount() == 0;
    }

    private void update() {
        if (hasSelectedRow()) {
            JOptionPane.showMessageDialog(this, "Por favor, elije un item");
            return;
        }

        Optional.ofNullable(model.getValueAt(table.getSelectedRow(), table.getSelectedColumn()))
                .ifPresentOrElse(row -> {
                    Integer id = Integer.valueOf(model.getValueAt(table.getSelectedRow(), 0).toString());
                    String name = (String) model.getValueAt(table.getSelectedRow(), 1);
                    String description = (String) model.getValueAt(table.getSelectedRow(), 2);
                    Integer quantity = Integer.valueOf(model.getValueAt(table.getSelectedRow(), 3).toString());

                    var rowsModified = this.productController.update(name, description, quantity, id);

                    JOptionPane.showMessageDialog(this, String.format("item modificado con éxito! %d", rowsModified));
                }, () -> JOptionPane.showMessageDialog(this, "Por favor, elije un item"));
    }

    private void delete() {
        if (hasSelectedRow()) {
            JOptionPane.showMessageDialog(this, "Por favor, elije un item");
            return;
        }

        Optional.ofNullable(model.getValueAt(table.getSelectedRow(), table.getSelectedColumn()))
                .ifPresentOrElse(fila -> {
                    Integer id = Integer.valueOf(model.getValueAt(table.getSelectedRow(), 0).toString());

                    int quantityEliminated = this.productController.delete(id);

                    model.removeRow(table.getSelectedRow());

                    JOptionPane.showMessageDialog(this, quantityEliminated + " Item eliminado con éxito!");
                }, () -> JOptionPane.showMessageDialog(this, "Por favor, elije un item"));
    }

    private void loadTable() {
        var products = this.productController.list();
        products.forEach(product -> model.addRow(new Object[]{
                product.getId(), product.getName(), product.getDescription(), product.getQuantity()
        }));
    }

    private void save() {
        if (textName.getText().isBlank() || textDescription.getText().isBlank() ||
                Objects.requireNonNull(comboCategory.getSelectedItem()).toString().equals("Elige una Categoría"))  {
            JOptionPane.showMessageDialog(this, "Los campos Nombre, Descripción y Categoría son requeridos.");
            return;
        }

        int quantityInt;

        try {
            quantityInt = Integer.parseInt(textQuantity.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, String
                    .format("El campo cantidad debe ser numérico dentro del rango %d y %d.", 0, Integer.MAX_VALUE));
            return;
        }

        var product = new Product(textName.getText(), textDescription.getText(), quantityInt);

        var category = (Category) comboCategory.getSelectedItem();

        this.productController.save(product, category.getId());

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
