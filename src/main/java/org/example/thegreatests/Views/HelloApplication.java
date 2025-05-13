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

        Employees foundEmployee;
        try {
            String url = "jdbc:sqlite:database.db";
            JdbcConnectionSource source = new JdbcConnectionSource(url);

            TableUtils.createTableIfNotExists(source, Employees.class);

            BaseDao<Employees, Integer> employeesDao = new BaseDao<>(source, Employees.class);

            Employees employee = new Employees("John Doe", "Developer", 40);
            employeesDao.create(employee);

            foundEmployee = employeesDao.findById(employee.getId());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/thegreatests/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle(
                "Hello!"
//                "Found employee: " + foundEmployee.getName() + ", Job: " + foundEmployee.getJob() + ", Worked Hours: " + foundEmployee.getWorkedHours()
        );
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) { launch();}
}