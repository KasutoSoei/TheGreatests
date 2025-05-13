package org.example.thegreatests.Controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import org.example.thegreatests.Models.BaseDao;
import org.example.thegreatests.Models.Dishes;
import javafx.scene.control.Label;


import java.awt.*;
import java.sql.SQLException;
import java.util.List;
public class DishesController {

    @FXML
    private ListView<String> MyListView;

    public void initialize() {
        loadDishes();
    }

    private void loadDishes() {
        try {
            String url = "jdbc:sqlite:database.db";
            JdbcConnectionSource source = new JdbcConnectionSource(url);

            TableUtils.createTableIfNotExists(source, Dishes.class);
            BaseDao<Dishes, Integer> DishDao = new BaseDao<>(source, Dishes.class);

            List<Dishes> dishesList = DishDao.findAll();

           dishesList.stream()
                   .forEach(dish -> MyListView.getItems().add(String.format("%s - %s (%.2f€)", dish.getName(), dish.getDescription(), dish.getPrice(), dish.getImage())));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleCreateDish (ActionEvent event){

        Pane panel = new Pane();
        panel.setStyle("-fx-background-color: black;");
        Label label = new Label("AJouter un plat");
        panel.getChildren().add(label);
    }
}
