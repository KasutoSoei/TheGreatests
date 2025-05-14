package org.example.thegreatests.Controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.thegreatests.Models.BaseDao;
import org.example.thegreatests.Models.Invoices;
import org.example.thegreatests.Views.HelloApplication;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

public class PDFInvoicesController {

    @FXML
    private Pane page;

    @FXML
    private VBox vBoxInvoices;

    @FXML
    private Label labelExpenses;

    @FXML
    private Label labelProfit;

    private float profits;
    private float expenses;

    @FXML
    public void initialize() {
        System.out.println("Exportation en PDF");
        setProfitsAndExpenses();
        showInvoices();
        PrinterJob job = PrinterJob.createPrinterJob();
        if(job != null){
            job.printPage(page);
            job.endJob();
        }

    }

    private void setProfitsAndExpenses() {
        List<Invoices> invoices = getInvoicesInfos();
        profits = 0;
        expenses = 0;
        invoices.stream().forEach(i->{
            if (i.getTotalPrice() >= 0) {
                profits += i.getTotalPrice();
            } else {
                expenses += i.getTotalPrice();
            }
        });
        labelProfit.setText("Bénéfices : "+String.valueOf(profits)+"€");
        labelExpenses.setText("Dépenses : "+String.valueOf(expenses)+"€");
    }

    private BaseDao<Invoices, Integer> initInvoicesDao() {
        try {
            String url = "jdbc:sqlite:database.db";
            JdbcConnectionSource source = new JdbcConnectionSource(url);
            TableUtils.createTableIfNotExists(source, Invoices.class);
            return new BaseDao<>(source, Invoices.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Invoices> getInvoicesInfos() {
        try {
            BaseDao<Invoices, Integer> tableDao = initInvoicesDao();
            return tableDao.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void showInvoices() {

        vBoxInvoices.getChildren().clear();

        List<Invoices> invoices = getInvoicesInfos();
        invoices.stream().forEach(i->{
            VBox invoicePane = new VBox();
            HBox invoiceTitleBox = new HBox();

            Label descriptionLabel = new Label(i.getDescription());
            Label statusLabel = new Label(i.getStatus());
            Label totalPriceLabel = new Label(String.valueOf(i.getTotalPrice())+"€");

            invoicePane.styleProperty().setValue("-fx-background-color: red;");
            if (i.getTotalPrice() >= 0) {
                invoicePane.styleProperty().setValue("-fx-background-color: lime;");
            }

            descriptionLabel.setStyle("color: gray");

            invoiceTitleBox.getChildren().addAll(statusLabel, totalPriceLabel);

            invoicePane.getChildren().addAll(invoiceTitleBox, descriptionLabel);
            vBoxInvoices.getChildren().add(invoicePane);
            vBoxInvoices.setPrefHeight(vBoxInvoices.getPrefHeight());
        });

    }


}