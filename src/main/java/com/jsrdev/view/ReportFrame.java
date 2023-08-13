package com.jsrdev.view;

import com.jsrdev.controller.CategoryController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.Serial;

public class ReportFrame extends JFrame {

    @Serial
    private static final long serialVersionUID = 1L;

    private JTable tableReport;
    private final DefaultTableModel model;

    private final CategoryController categoryController;

    public ReportFrame(ControlStockFrame controlDeStockFrame) {
        super("Reporte de produtos del stock");

        this.categoryController = new CategoryController();

        Container container = getContentPane();
        setLayout(null);

        tableReport = new JTable();
        tableReport.setBounds(0, 0, 600, 400);
        container.add(tableReport);

        model = (DefaultTableModel) tableReport.getModel();
        model.addColumn("");
        model.addColumn("");
        model.addColumn("");
        model.addColumn("");

        reportUpload(); //cargar reporte

        setSize(600, 400);
        setVisible(true);
        setLocationRelativeTo(controlDeStockFrame);
    }

    private void reportUpload() {
        var content = categoryController.reportUpload();
        
        // TODO
        content.forEach(fila -> model
                .addRow(new Object[] {}));
    }

}
