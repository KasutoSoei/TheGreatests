package org.example.thegreatests.Views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class InvoicesView extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InvoicesView.class.getResource("/org/example/thegreatests/invoices-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("Gestion des factures !");
        stage.setScene(scene);
        stage.show();


    }

    public static void main(String[] args) { launch();}
}