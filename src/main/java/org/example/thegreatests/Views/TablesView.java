package org.example.thegreatests.Views;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.thegreatests.Controllers.TablesController;
import org.example.thegreatests.Models.BaseDao;
import org.example.thegreatests.Models.Table;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class TablesView extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TablesView.class.getResource("/org/example/thegreatests/tables-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("Gestion des tables!");
        stage.setScene(scene);
        stage.show();


    }

    public static void main(String[] args) { launch();}
}