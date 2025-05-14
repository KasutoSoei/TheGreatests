package org.example.thegreatests.Views;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.thegreatests.Models.BaseDao;
import org.example.thegreatests.Models.Employees;

import java.io.IOException;
import java.sql.SQLException;

public class EmployeeView extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(EmployeeView.class.getResource("/org/example/thegreatests/employee-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("Gestion des employées!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) { launch();}
}