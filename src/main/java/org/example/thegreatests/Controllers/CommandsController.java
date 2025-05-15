package org.example.thegreatests.Controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.thegreatests.Models.BaseDao;
import org.example.thegreatests.Models.Commands;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CommandsController {

    @FXML
    private TableView<Commands> CommandList;

    @FXML
    private TableColumn<Commands, Integer> idColumn;

    @FXML
    private TableColumn<Commands, String> statusColumn;

    @FXML
    private TableColumn<Commands, String> dateColumn;

    @FXML
    private TableColumn<Commands, String> actionColumn;

    @FXML
    private TableView<Commands> CommandFinishList;

    @FXML
    private TableColumn<Commands, Integer> finishedIdColumn;

    @FXML
    private TableColumn<Commands, String> finishedStatusColumn;

    @FXML
    private TableColumn<Commands, String> finishedDateColumn;

    @FXML
    private Pane pane;

    private final ObservableList<Commands> commandsObservableList = FXCollections.observableArrayList();
    private final ObservableList<Commands> finishedCommandsObservableList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize columns for CommandList
        idColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
        statusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
        dateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDate()));
        actionColumn.setCellFactory(column -> new TableCell<>() {
            private final Button doneButton = new Button("Terminer");
            private final Button cancelButton = new Button("Annuler");

            {
                doneButton.setOnAction(e -> {
                    Commands command = getTableView().getItems().get(getIndex());
                    updateCommandStatus(command, "Terminé");
                });
                cancelButton.setOnAction(e -> {
                    Commands command = getTableView().getItems().get(getIndex());
                    updateCommandStatus(command, "Annulée");
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(5, doneButton, cancelButton);
                    setGraphic(buttons);
                }
            }
        });

        // Initialize columns for CommandFinishList
        finishedIdColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
        finishedStatusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
        finishedDateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDate()));

        // Load data
        loadCommands();
        loadCommandsFinish();
    }

    private BaseDao<Commands, Integer> initCommandDishDao() {
        try {
            String url = "jdbc:sqlite:database.db";
            JdbcConnectionSource source = new JdbcConnectionSource(url);
            TableUtils.createTableIfNotExists(source, Commands.class);
            return new BaseDao<>(source, Commands.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadCommands() {
        commandsObservableList.clear();
        try {
            BaseDao<Commands, Integer> commandsDao = initCommandDishDao();
            List<Commands> foundCommands = commandsDao.findAll();
            commandsObservableList.addAll(foundCommands.stream()
                    .filter(command -> command.getStatus().equals("En attente"))
                    .toList());
            CommandList.setItems(commandsObservableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadCommandsFinish() {
        finishedCommandsObservableList.clear();
        try {
            BaseDao<Commands, Integer> commandsDao = initCommandDishDao();
            List<Commands> foundCommands = commandsDao.findAll();
            finishedCommandsObservableList.addAll(foundCommands.stream()
                    .filter(command -> command.getStatus().equals("Terminé"))
                    .toList());
            CommandFinishList.setItems(finishedCommandsObservableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateCommandStatus(Commands command, String newStatus) {
        try {
            BaseDao<Commands, Integer> commandsDao = initCommandDishDao();
            command.setStatus(newStatus);
            commandsDao.update(command);
            loadCommands();
            loadCommandsFinish();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onClickBack() {
        changeScene("/org/example/thegreatests/main-view.fxml");
    }

    private void changeScene(String resourcePath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resourcePath));
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