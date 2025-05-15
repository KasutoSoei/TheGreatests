package org.example.thegreatests.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Pane paneDashboard;

    public void handleChangeScene(ActionEvent event) throws IOException {
        System.out.println("Main Table");
    }

    @FXML
    private void onClickViewTables() {
        changeScene("/org/example/thegreatests/tables-view.fxml");

    }

    @FXML
    private void onClickViewCommands() {
        changeScene("/org/example/thegreatests/commands-view.fxml");

    }

    @FXML
    private void onClickViewEmployees() {
        changeScene("/org/example/thegreatests/employee-view.fxml");

    }

    @FXML
    private void onClickViewDishes() {
        changeScene("/org/example/thegreatests/DishesPage-view.fxml");

    }

    @FXML
    private void onClickViewInvoices() {
        changeScene("/org/example/thegreatests/invoices-view.fxml");

    }

    private void changeScene(String ressourcePath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ressourcePath));
            Parent root = loader.load();
            Stage stage = (Stage) paneDashboard.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}