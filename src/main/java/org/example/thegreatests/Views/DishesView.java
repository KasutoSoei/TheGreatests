package org.example.thegreatests.Views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.thegreatests.Models.Dishes;

import java.io.IOException;

public class DishesView extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        Dishes foundDish;

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/thegreatests/DishesPage-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1080, 720);
        stage.setTitle("Dishes Page");
        stage.setScene(scene);
        stage.show();

    }



    public static void main(String[] args) { launch();}
}