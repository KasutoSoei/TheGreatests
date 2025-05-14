package org.example.thegreatests.Controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.example.thegreatests.Models.BaseDao;
import org.example.thegreatests.Models.Dishes;
import javafx.scene.control.Label;
import javafx.geometry.Insets;


import javafx.scene.image.Image;
import java.sql.SQLException;
import java.util.List;
public class DishesController {

    @FXML
    private ListView<HBox> MyListView;

    // Calls LoadDishes at page start
    public void initialize() {
        loadDishes();
    }

    // Method getting dishes in database and displaying them
    private void loadDishes() {
        try {
            String url = "jdbc:sqlite:database.db";
            JdbcConnectionSource source = new JdbcConnectionSource(url);

            TableUtils.createTableIfNotExists(source, Dishes.class);
            BaseDao<Dishes, Integer> DishDao = new BaseDao<>(source, Dishes.class);

            List<Dishes> foundDishes = DishDao.findAll();

            foundDishes.stream().forEach(dish -> {
                ImageView img = new ImageView(new Image(dish.getImage(), true));
                img.setFitWidth(80);
                img.setFitHeight(60);

                Button deleteBtn = new Button("Supprimer");
                deleteBtn.setOnAction(e -> {
                    try {
                        DishDao.deleteById(dish.getId());
                        MyListView.getItems().clear();
                        loadDishes();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                });

                Label label = new Label(dish.getName() + " - " + dish.getPrice() + "€");
                label.setStyle("-fx-font-size: 25px;");
                HBox hbox = new HBox(10, img, label, deleteBtn);
                hbox.setPadding(new Insets(5));

                MyListView.getItems().add(hbox);

            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Method opening a popup to create a new dish
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
                MyListView.getItems().clear();
                loadDishes();


            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });



        panel.getChildren().addAll(nameInput, descInput, priceInput, imageInput, submitButton);

        Scene scene = new Scene(panel, 700, 600);


        popup.setTitle("Nouveau plat");
        popup.setScene(scene);
        popup.show();
    }

}
