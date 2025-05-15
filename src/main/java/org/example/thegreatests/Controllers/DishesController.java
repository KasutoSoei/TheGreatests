package org.example.thegreatests.Controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.thegreatests.Models.BaseDao;
import org.example.thegreatests.Models.Dishes;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.scene.image.Image;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


public class DishesController {

    @FXML
    private ListView<HBox> MyListView;
    private TextField indexError;

    @FXML
    private AnchorPane pane;

    @FXML
    private Label expensiveDish;

    @FXML
    private Label affordableDish;

    @FXML
    private Label totalPrice;

    @FXML
    private TextField searchBar;

    @FXML
    private Button SearchBtn;

    /**
     * This method is used to initialize the DishesController.
     */
    public void initialize() {
        displayDishes(null);

        SearchBtn.setOnAction(e -> {
            String keyword = searchBar.getText().trim();
            displayDishes(keyword);
        });
    }

    /**
     * This method is used to initialize the order with minimum and maximum prices and sum of all dishes.
     */
    private void priceIndicators() {
        List<Dishes> Dishes = getDishesInfos();
        Optional<Dishes> mostExpensive = Dishes.stream()
                .max((d1, d2) -> Float.compare(d1.getPrice(), d2.getPrice()));

        Optional<Dishes> leastExpensive = Dishes.stream()
                .min((d1, d2) -> Float.compare(d1.getPrice(), d2.getPrice()));

        float total = (float) Dishes.stream()
                .mapToDouble(dish -> dish.getPrice())
                .sum();

        if (mostExpensive.isPresent()) {
            Dishes dish = mostExpensive.get();
            expensiveDish.setText("Plat le moins cher : " + dish.getName() + " " + dish.getPrice() + " €");
        } else {
            expensiveDish.setText("Aucun plat trouvé");
        }

        if (leastExpensive.isPresent()) {
            Dishes dish = leastExpensive.get();
            affordableDish.setText("Plat le plus cher : "+ dish.getName() + " " + dish.getPrice() + " €");
        } else {
            affordableDish.setText("Aucun plat trouvé");
        }

        totalPrice.setText(String.valueOf(total + " €"));
    }

    /**
     * This function is used to initialize the tables DAO.
     * @return BaseDao<Table, Integer> The tables DAO.
     */
    private BaseDao<Dishes, Integer> initDishesDao() {

        try {
            String url = "jdbc:sqlite:database.db";
            JdbcConnectionSource source = new JdbcConnectionSource(url);
            TableUtils.createTableIfNotExists(source, Dishes.class);
            BaseDao<Dishes, Integer> DishesDao = new BaseDao<>(source, Dishes.class);
            return DishesDao;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This function is used to get the tables information from the database.
     * @return List<Table> The list of tables.
     */
    private List<Dishes> getDishesInfos() {
        System.out.println("J'ai cliqué sur le bouton");
        try {
            BaseDao<Dishes, Integer> DishesDao = initDishesDao();
            List<Dishes> FoudDishes = DishesDao.findAll();
            return FoudDishes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is used to initialize the Dishes in listview and search bar.
     */
    private void displayDishes(String keyword) {
        MyListView.getItems().clear();
        BaseDao<Dishes, Integer> dishesDao = initDishesDao();
        List<Dishes> allDishes = getDishesInfos();

        allDishes.stream()
                .filter(dish -> keyword == null || keyword.isEmpty() || dish.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                .forEach(dish -> {
                    ImageView img = new ImageView(new Image(dish.getImage(), true));
                    img.setFitWidth(80);
                    img.setFitHeight(60);

                    Button deleteBtn = new Button("Supprimer");
                    deleteBtn.setOnAction(e -> {
                        try {
                            dishesDao.deleteById(dish.getId());
                            displayDishes(keyword); // recharge après suppression
                            priceIndicators();
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    });

                    Label label = new Label(dish.getName());
                    label.setStyle("-fx-font-size: 25px;");

                    VBox labelContainer = new VBox(label);
                    labelContainer.setAlignment(Pos.CENTER);
                    VBox deleteBtnContainer = new VBox(deleteBtn);
                    deleteBtnContainer.setAlignment(Pos.CENTER);

                    HBox hbox = new HBox(10, img, labelContainer, deleteBtnContainer);
                    hbox.setPadding(new Insets(5));

                    hbox.setOnMouseClicked(event -> handleDishClicked(dish.getDescription(), dish.getPrice()));

                    MyListView.getItems().add(hbox);
                });

        priceIndicators(); // à appeler une seule fois ici
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
                    displayDishes(null);


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

    @FXML
    private void onClickBack() {
        changeScene("/org/example/thegreatests/main-view.fxml");

    }

    private void changeScene(String ressourcePath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ressourcePath));
            Parent root = loader.load();
            Stage stage = (Stage) pane.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    }
