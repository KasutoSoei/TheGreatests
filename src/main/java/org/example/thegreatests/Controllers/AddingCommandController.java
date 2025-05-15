package org.example.thegreatests.Controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.thegreatests.Models.BaseDao;
import org.example.thegreatests.Models.Commands;
import org.example.thegreatests.Models.Dishes;
import org.example.thegreatests.Models.Table;
import org.example.thegreatests.Views.CommandsView;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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


    @FXML
    public void initialize() {
        System.out.println("Controller Adding Command");
        showDishesGrid();
        errorLabel.setVisible(false);
        List<Table> tablesInfo = getTablesInfos();
        List<String> tablesLocations = tablesInfo.stream()
                .map(table -> "Table n°" + table.getLocation())
                .toList();

        ObservableList<String> list = FXCollections.observableArrayList(tablesLocations);
        selectTable.setItems(list);



    }

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

    private void addingDishesToCommand(Dishes d) {
        System.out.println("Ajout de " + d.getName() + " à la commande");
        HBox dishBox = new HBox();
        dishBox.setSpacing(10);
        ImageView img = new ImageView(new Image(d.getImage(), true));
        img.setFitWidth(80);
        img.setFitHeight(60);
        Label name = new Label(d.getName());
        Label price = new Label(d.getPrice()+" €");
        dishBox.getChildren().addAll(img,name,price);
        vBoxCommand.getChildren().add(dishBox);
        vBoxCommand.setPrefHeight(vBoxCommand.getPrefHeight());
    }

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
            gridPane.getChildren().add(cell);
        });

    }

    @FXML
    private void onValidatedCommand() {
        BaseDao<Commands, Integer> tableDao = initCommandsDao();
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

        Commands command = new Commands(dishiesList, idTable,"En attente", formatter.format(now));

        try {
            if (vBoxCommand.getChildren().isEmpty()){
                errorLabel.setText("La commande doit contenir au moins un plat");
                errorLabel.setStyle("-fx-text-fill: red;");
                errorLabel.setVisible(true);
            }
            else {
                tableDao.create(command);
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
    }
}