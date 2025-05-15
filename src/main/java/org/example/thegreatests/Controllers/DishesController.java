package org.example.thegreatests.Controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
    private TextField indexError;

    /**
     * This method is used to initialize the DishesController.
     */
    public void initialize() {
        loadDishes();
    }

    /**
     * This method is used to load the dishes from the database and display them in the ListView.
     */
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

                Label label = new Label(dish.getName());
                label.setStyle("-fx-font-size: 25px;");

                String desc = dish.getDescription();
                Float price = dish.getPrice();

                VBox labelContainer = new VBox(label);
                labelContainer.setAlignment(Pos.CENTER);
                VBox deleteBtnContainer = new VBox(deleteBtn);
                deleteBtnContainer.setAlignment(Pos.CENTER);
                HBox hbox = new HBox(10, img, labelContainer, deleteBtnContainer);
                hbox.setPadding(new Insets(5));

                hbox.setOnMouseClicked(event -> {
                    handleDishClicked(desc, price);
                });

                MyListView.getItems().add(hbox);

            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * This method is used to handle the create dish button click event.
     */
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

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Label emptyErrorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Button submitButton = new Button("Create");
        submitButton.setOnAction(e -> {

            panel.getChildren().remove(errorLabel);

            String name = nameInput.getText();
            String desc = descInput.getText();
            String priceTxt = priceInput.getText();
            String imageURL = imageInput.getText();


            String url = "jdbc:sqlite:database.db";
            JdbcConnectionSource source = null;


            if (name.isEmpty() || desc.isEmpty() || imageURL.isEmpty()) {
                errorLabel.setText("All fields must be filled");
                panel.getChildren().add(errorLabel);
            }

            try {

                    float price = Float.parseFloat(priceTxt);

                    source = new JdbcConnectionSource(url);
                    TableUtils.createTableIfNotExists(source, Dishes.class);
                    BaseDao<Dishes, Integer> DishDao = new BaseDao<>(source, Dishes.class);

                    Dishes dish = new Dishes(name, desc, price, imageURL);

                    DishDao.create(dish);

                    popup.close();
                    MyListView.getItems().clear();
                    loadDishes();

            } catch (NumberFormatException ex) {
                if (!panel.getChildren().contains(errorLabel)) {
                    errorLabel.setText("Please enter a valid price");
                    panel.getChildren().add(panel.getChildren().indexOf(priceInput) + 1, errorLabel);
                }
            }
            catch (SQLException ex) {
                throw new RuntimeException(ex);

            }
        });


        panel.getChildren().addAll(nameInput, descInput, priceInput, imageInput, submitButton);

        Scene scene = new Scene(panel, 700, 600);


        popup.setTitle("Nouveau plat");
        popup.setScene(scene);
        popup.show();
    }

    /**
     * This method is used to handle the dish clicked event.
     */
    private void handleDishClicked(String desc, float price){

        HBox selectedDish = MyListView.getSelectionModel().getSelectedItem();

        VBox detailsPanel = new VBox(10);
        detailsPanel.setStyle("-fx-background-color: lightblue;");
        detailsPanel.setPadding(new Insets(10));

        Stage dishDetails = new Stage();
        dishDetails.setTitle("Détails du plat");

        Label dishDesc = new Label();
        dishDesc.setText(desc);
        dishDesc.setStyle("-fx-font-size: 20px;");

        Label dishPrice = new Label();
        dishPrice.setText(price + "€");
        dishPrice.setStyle("-fx-font-size: 20px;");


        detailsPanel.getChildren().addAll(dishDesc, dishPrice);

        Scene scene = new Scene(detailsPanel, 400, 140);

        dishDetails.setScene(scene);
        dishDetails.show();

        }



    }
