package org.example.thegreatests.Controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.query.In;
import com.j256.ormlite.table.TableUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.thegreatests.Models.BaseDao;
import org.example.thegreatests.Models.Invoices;
import org.example.thegreatests.Models.Table;
import org.example.thegreatests.Views.HelloApplication;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

public class InvoicesController {



    private float profits;
    private float expenses;

    @FXML
    private Label labelTotalProfits;

    @FXML
    private Label labelTotalExpenses;

    @FXML
    private VBox vBoxInvoices;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Pane paneFactureCreation;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField textFieldPrice;

    @FXML
    private Label labelErrorInvoiceCreation;

    @FXML
    private TextArea textInvoiceDescription;

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

    @FXML
    public void initialize() {
        System.out.println("Controller Invoice");
        //BaseDao<Invoices, Integer> invoiceDao = initInvoicesDao();
        //try {
        //    Invoices invoice = new Invoices("Test", "En attente", 100, new java.util.Date());
        //    invoiceDao.create(invoice);
        //} catch (SQLException e) {
        //    throw new RuntimeException(e);
        //}
        setProfitsAndExpenses();
        showInvoices();
    }

    @FXML
    public void updateText() {
        int caretPos = textFieldPrice.getCaretPosition(); // Garder la position du input pour éviter qu'il se retrouve au début
        String input = textFieldPrice.getText().replaceAll("[^\\d.,-]", "");
        if (input.isEmpty()) {
            textFieldPrice.setText("");
            return;
        }
        try {
            double value = Double.parseDouble(input.replace(',', '.'));
            DecimalFormat myFormat = new DecimalFormat("#####0.00€;-#####0.00€");
            String formatted = myFormat.format(value);
            textFieldPrice.setText(formatted);
            textFieldPrice.positionCaret(Math.min(caretPos, formatted.length()));
        } catch (NumberFormatException e) {
            textFieldPrice.setText("");
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
        labelTotalProfits.setText("Bénéfices : "+String.valueOf(profits)+"€");
        labelTotalExpenses.setText("Dépenses : "+String.valueOf(expenses)+"€");
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
            invoicePane.setPadding(new javafx.geometry.Insets(10));

            Label descriptionLabel = new Label(i.getDescription());
            Label statusLabel = new Label(i.getStatus());
            Label totalPriceLabel = new Label(String.valueOf(i.getTotalPrice())+"€");
            totalPriceLabel.setStyle("-fx-font-size: 32px;");

            invoicePane.styleProperty().setValue("-fx-background-color: red;");
            if (i.getTotalPrice() >= 0) {
                invoicePane.styleProperty().setValue("-fx-background-color: lime;");
            }

            invoicePane.getChildren().addAll(descriptionLabel,statusLabel,totalPriceLabel);
            vBoxInvoices.getChildren().add(invoicePane);
            vBoxInvoices.setPrefHeight(vBoxInvoices.getPrefHeight());
        });

    }


    @FXML
    private void closeInvoiceCreationModal() {
        paneFactureCreation.setVisible(false);

    }

    @FXML void openInvoiceCreationModal() {
        paneFactureCreation.setVisible(true);

        datePicker.setValue(java.time.LocalDate.now());
        labelErrorInvoiceCreation.setText("");
    }

    @FXML void onCreateInvoice() {

        if (textFieldPrice.getText().isEmpty()) {
            labelErrorInvoiceCreation.setText("Erreur : le prix est vide");
            return;
        }
        if (datePicker.getValue() == null) {
            labelErrorInvoiceCreation.setText("Erreur : la date est vide");
            return;
        }
        if (textInvoiceDescription.getText().isEmpty()) {
            labelErrorInvoiceCreation.setText("Erreur : la description est vide");
            return;
        }

        String description = textInvoiceDescription.getText();
        String status = "Payée";
        float totalPrice = 0;
        try {
            String totalPriceString = textFieldPrice.getText().replaceAll("€","").replaceAll(",",".");
            totalPrice = Float.parseFloat(totalPriceString);
        } catch (NumberFormatException e) {
            labelErrorInvoiceCreation.setText("Erreur de format de prix : "+e.getMessage());
            System.out.println("Erreur de format de prix : "+e.getMessage());
            return;
        }
        Invoices invoice = new Invoices(description, status, totalPrice, new java.util.Date());
        try {
            BaseDao<Invoices, Integer> invoiceDao = initInvoicesDao();
            invoiceDao.create(invoice);
            setProfitsAndExpenses();
            showInvoices();
            closeInvoiceCreationModal();
        } catch (SQLException e) {
            System.out.println("Erreur de BDD : "+e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onExportPDF() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/thegreatests/pdf-invoices.fxml"));
        fxmlLoader.load();
    }

}