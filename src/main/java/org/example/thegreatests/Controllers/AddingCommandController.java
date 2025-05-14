package org.example.thegreatests.Controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
import java.util.List;

public class AddingCommandController {

    private List<Dishes> dishiesList;

    @FXML
    private GridPane gridPane;

    @FXML
    private VBox vBoxCommand;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    public void initialize() {
        System.out.println("Controller Adding Command");
        showDishesGrid();


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
        Commands command = new Commands(dishiesList, 1,"En attente");
        try {
            tableDao.create(command);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}