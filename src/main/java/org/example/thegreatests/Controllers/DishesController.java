package org.example.thegreatests.Controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.thegreatests.Models.BaseDao;
import org.example.thegreatests.Models.Dishes;
import javafx.scene.control.Label;
import javafx.geometry.Insets;


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

            Dishes dish1 = new Dishes("Pâtes à la carbonara", "Bah des pâtes carbo quoi", 12.5f, "https://www.panzani.fr/_ipx/f_webp&q_80&s_1800x1202/https://backend.panzani.fr/app/uploads/2023/10/visuel-spaghetti-carbonara-unsmushed.png");
            //DishDao.create(dish1);

            List<Dishes> foundDishes = DishDao.findAll();

            foundDishes.stream()
                   .forEach(dish -> MyListView.getItems().addAll(dish.getName() + " - " + dish.getDescription() + " - " + dish.getPrice()+"€"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCreateDish (){

        VBox panel = new VBox(10);
        panel.setStyle("-fx-background-color: lightblue;");
        panel.setPadding(new Insets(10));

        Stage popup = new Stage();

        TextField nameInput = new TextField();
        nameInput.setPromptText("Name");

        TextField descInput = new TextField();
        descInput.setPromptText("Description");

        TextField priceInput = new TextField();
        priceInput.setPromptText("Price");

        TextField imageInput = new TextField();
        imageInput.setPromptText("Image source");

        Button submitButton = new Button("Create");
        submitButton.setOnAction(e -> {
            String name = nameInput.getText();
            String desc = descInput.getText();
            float price = Float.parseFloat(priceInput.getText());
            String imageURL = imageInput.getText();

            String url = "jdbc:sqlite:database.db";
            JdbcConnectionSource source = null;
            try {
                source = new JdbcConnectionSource(url);
                TableUtils.createTableIfNotExists(source, Dishes.class);
                BaseDao<Dishes, Integer> DishDao = new BaseDao<>(source, Dishes.class);

                Dishes dish = new Dishes(name, desc, price, imageURL);
                DishDao.create(dish);

                popup.close();

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });



        panel.getChildren().addAll(nameInput, descInput, priceInput, imageInput, submitButton);

        Scene scene = new Scene(panel, 300, 200);


        popup.setTitle("Nouveau plat");
        popup.setScene(scene);
        popup.show();
    }

    private void reloadDishes() {
        //
    }
}
