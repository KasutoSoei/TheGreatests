package org.example.thegreatests.Controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.thegreatests.Models.*;
import org.example.thegreatests.Views.CommandsView;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

public class AddingCommandController {

    private List<Dishes> dishiesList;

    @FXML
    private GridPane gridPane;

    @FXML
    private VBox vBoxCommand;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Label errorLabel;

    @FXML
    private ComboBox <String> selectTable;

    private String SelectValue;

    private final Map<Dishes, Integer> dishesCountMap = new HashMap<>();

    @FXML
    private Pane pane;

    /**
     * This method is used to initialize the AddingCommandController.
     */
    @FXML
    public void initialize() {

        showDishesGrid();
        errorLabel.setVisible(false);
        List<Table> tablesInfo = getTablesInfos();
        List<String> tablesLocations = tablesInfo.stream()
                .map(table -> "Table n°" + table.getLocation())
                .toList();

        ObservableList<String> list = FXCollections.observableArrayList(tablesLocations);
        selectTable.setItems(list);



    }

    /**
     * This function is used to initialize the dishes DAO.
     * @return BaseDao<Dishes, Integer> The dishes DAO.
     */
    private BaseDao<Dishes, Integer> initDishesDao() {

        try {
            String url = "jdbc:sqlite:database.db";
            JdbcConnectionSource source = new JdbcConnectionSource(url);
            TableUtils.createTableIfNotExists(source, Dishes.class);
            BaseDao<Dishes, Integer> dishesDao = new BaseDao<>(source, Dishes.class);
            return dishesDao;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This function is used to initialize the tables DAO.
     * @return BaseDao<Table, Integer> The tables DAO.
     */
    private BaseDao<Table, Integer> initTablesDao() {

        try {
            String url = "jdbc:sqlite:database.db";
            JdbcConnectionSource source = new JdbcConnectionSource(url);
            TableUtils.createTableIfNotExists(source, Table.class);
            BaseDao<Table, Integer> tablesDao = new BaseDao<>(source, Table.class);
            return tablesDao;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This function is used to initialize the commands DAO.
     * @return BaseDao<Commands, Integer> The commands DAO.
     */
    private BaseDao<Commands, Integer> initCommandsDao() {
        try {
            String url = "jdbc:sqlite:database.db";
            JdbcConnectionSource source = new JdbcConnectionSource(url);
            TableUtils.createTableIfNotExists(source, Commands.class);
            BaseDao<Commands, Integer> commandsDao = new BaseDao<>(source, Commands.class);
            return commandsDao;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private BaseDao<CommandDish, Integer> initCommandDishDao() {
        try {
            String url = "jdbc:sqlite:database.db";
            JdbcConnectionSource source = new JdbcConnectionSource(url);
            TableUtils.createTableIfNotExists(source, CommandDish.class);
            BaseDao<CommandDish, Integer> commandDishDao = new BaseDao<>(source, CommandDish.class);
            return commandDishDao;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This function is used to get the dishes information from the database.
     * @return List<Dishes> The list of dishes.
     */
    private List<Dishes> getDishesInfos() {
        System.out.println("J'ai cliqué sur le bouton");
        try {
            BaseDao<Dishes, Integer> dishesDao = initDishesDao();
            List<Dishes> foundDishes = dishesDao.findAll();
            return foundDishes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This function is used to get the tables information from the database.
     * @return List<Table> The list of tables.
     */
    private List<Table> getTablesInfos() {
        System.out.println("J'ai cliqué sur le bouton");
        try {
            BaseDao<Table, Integer> tablesDao = initTablesDao();
            List<Table> foundTable = tablesDao.findAll();
            return foundTable;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is used to add a dish to the command.
     * @param dish The dish to add.
     */
    private void addingDishesToCommand(Dishes dish) {
        // Met à jour la quantité du plat
        dishesCountMap.put(dish, dishesCountMap.getOrDefault(dish, 0) + 1);

        // Rafraîchit l'affichage
        refreshCommandDisplay();
    }

    private void refreshCommandDisplay() {
        vBoxCommand.getChildren().clear();

        for (Map.Entry<Dishes, Integer> entry : dishesCountMap.entrySet()) {
            Dishes dish = entry.getKey();
            int quantity = entry.getValue();

            HBox dishBox = createDishBox(dish, quantity);
            vBoxCommand.getChildren().add(dishBox);
        }
    }
    private HBox createDishBox(Dishes dish, int quantity) {
        HBox dishBox = new HBox();
        dishBox.setSpacing(10);

        // Affichage de l'image
        ImageView img = new ImageView(new Image(dish.getImage(), true));
        img.setFitWidth(80);
        img.setFitHeight(60);

        // Nom + quantité + prix
        Label name = new Label(dish.getName() + " x" + quantity);
        Label price = new Label(dish.getPrice() + " €");

        // Bouton supprimer
        Button deleteBtn = new Button("retirer");
        deleteBtn.setOnAction(e -> {
            int currentQuantity = dishesCountMap.getOrDefault(dish, 0);
            if (currentQuantity > 1) {
                dishesCountMap.put(dish, currentQuantity - 1);
            } else {
                dishesCountMap.remove(dish);
            }
            refreshCommandDisplay(); // Mise à jour de l'affichage
        });

        dishBox.getChildren().addAll(img, name, price, deleteBtn);
        return dishBox;
    }

    /**
     * This method is used to show the dishes in the grid.
     */
    private void showDishesGrid() {

        List<Dishes> foundDishes = getDishesInfos();

        foundDishes.stream().forEach(d->{
            VBox cell = new VBox();
            Label name = new Label(d.getName());
            Button addingToCommand = new Button("Ajouter");
            addingToCommand.setOnAction(e -> {
                addingDishesToCommand(d);
            });
            System.out.println("Name: " + d.getName());
            cell.getChildren().addAll(name,addingToCommand);
            gridPane.add(cell, foundDishes.indexOf(d)%5, foundDishes.indexOf(d)/5);
        });

    }

    /**
     * This method is used to validate the command and add it to the database.
     */
    @FXML
    private void onValidatedCommand() {
        BaseDao<Commands, Integer> commandDao = initCommandsDao();
        BaseDao<CommandDish, Integer> commandDishDao = initCommandDishDao();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        SelectValue = selectTable.getValue();
        if (SelectValue == null || SelectValue.isEmpty()) {
            errorLabel.setText("Veuillez choisir une table");
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setVisible(true);
            return;
        }

        String TableValueLocation = SelectValue.replace("Table n°", "");

        List<Table> tablesInfo = getTablesInfos();
        Optional<Table> selectedTable = tablesInfo.stream()
                .filter(table -> Objects.equals(table.getLocation(), TableValueLocation))
                .findFirst();
        int idTable = selectedTable.get().getId();

        Commands command = new Commands(idTable,"En attente", formatter.format(now));
        GlobalChrono chrono = GlobalChrono.getInstance();
        try {
            if (chrono.isUnder15Minutes()){throw new SQLException();}
            if (vBoxCommand.getChildren().isEmpty()){
                errorLabel.setText("La commande doit contenir au moins un plat");
                errorLabel.setStyle("-fx-text-fill: red;");
                errorLabel.setVisible(true);
            }
            else {
                commandDao.create(command);
                vBoxCommand.getChildren().clear();
                errorLabel.setText("Commande ajoutée avec succès");
                errorLabel.setStyle("-fx-text-fill: green;");
                errorLabel.setVisible(true);
            }
        } catch (SQLException e) {
            errorLabel.setText("Impossible d'ajouter la commande");
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setVisible(true);
        }

        for (Map.Entry<Dishes, Integer> entry : dishesCountMap.entrySet()) {
            Dishes dish = entry.getKey();
            int quantity = entry.getValue();

            CommandDish commandDish = new CommandDish(command, dish, quantity);
            System.out.println("la commande : " + command.getId() + " dish : " + dish + "quantity : " + quantity);
            try {
                commandDishDao.create(commandDish);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        dishesCountMap.clear();
    }

    /**
     * This method is used to initialize the data for the AddingCommandController.
     * @param currentTableId The ID of the current table.
     */
    public void initData(Integer currentTableId) {
        try {
            BaseDao<Table, Integer> tablesDao = initTablesDao();
            List<Table> foundTable = tablesDao.findAll();
            Optional<Table> selectedTable = foundTable.stream()
                    .filter(table -> Objects.equals(table.getId(), currentTableId))
                    .findFirst();

            selectTable.setValue("Table n°" + selectedTable.get().getLocation());
            selectTable.setDisable(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onClickBackToTables() {
        changeScene("/org/example/thegreatests/tables-view.fxml");
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