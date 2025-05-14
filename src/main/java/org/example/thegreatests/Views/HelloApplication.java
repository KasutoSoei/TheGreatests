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

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/thegreatests/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1080, 720);
        stage.setTitle(
                "Hello!"
//                "Found employee: " + foundEmployee.getName() + ", Job: " + foundEmployee.getJob() + ", Worked Hours: " + foundEmployee.getWorkedHours()
        );
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) { launch();}
}