package org.example.thegreatests.Views;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.example.thegreatests.Models.BaseDao;
import org.example.thegreatests.Models.Dishes;
import javafx.geometry.Insets;
import java.io.IOException;
import java.sql.SQLException;

public class DishesPage extends Application {

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