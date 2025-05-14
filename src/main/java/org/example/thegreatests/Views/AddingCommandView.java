package org.example.thegreatests.Views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AddingCommandView extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AddingCommandView.class.getResource("/org/example/thegreatests/adding-command-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("Gestion des commandes!");
        stage.setScene(scene);
        stage.show();


    }

    public static void main(String[] args) { launch();}
}