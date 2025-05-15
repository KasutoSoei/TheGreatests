package org.example.thegreatests.Controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.thegreatests.Models.*;

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
        idColumn.setPrefWidth(60);
        idColumn.setStyle("-fx-alignment: CENTER;");
        statusColumn.setPrefWidth(122);
        statusColumn.setStyle("-fx-alignment: CENTER;");
        dateColumn.setPrefWidth(162);
        dateColumn.setStyle("-fx-alignment: CENTER;");
        actionColumn.setPrefWidth(154);

        // Initialise les colonnes de la liste des commandes en cours
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

        finishedIdColumn.setPrefWidth(60);
        finishedIdColumn.setStyle("-fx-alignment: CENTER;");
        finishedStatusColumn.setPrefWidth(122);
        finishedStatusColumn.setStyle("-fx-alignment: CENTER;");
        finishedDateColumn.setPrefWidth(162);
        finishedDateColumn.setStyle("-fx-alignment: CENTER;");

        // Initialise les colonnes de la liste des commandes terminées (servies et en attente de paiement)
        finishedIdColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
        finishedStatusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
        finishedDateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDate()));


        loadCommands();
        loadCommandsFinish();
    }

    private BaseDao<Commands, Integer> initCommandDao() {
        try {
            String url = "jdbc:sqlite:database.db";
            JdbcConnectionSource source = new JdbcConnectionSource(url);
            TableUtils.createTableIfNotExists(source, Commands.class);
            return new BaseDao<>(source, Commands.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private BaseDao<CommandDish, Integer> initCommandDishDao(){
        try {
            String url = "jdbc:sqlite:database.db";
            JdbcConnectionSource source = new JdbcConnectionSource(url);
            TableUtils.createTableIfNotExists(source, CommandDish.class);
            return new BaseDao<>(source, CommandDish.class);
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

    private void loadCommands() {
        commandsObservableList.clear();
        try {
            BaseDao<Commands, Integer> commandsDao = initCommandDao();
            List<Commands> foundCommands = commandsDao.findAll();
            commandsObservableList.addAll(foundCommands.stream()
                    .filter(command -> command.getStatus().equals("En attente"))
                    .toList());
            CommandList.setItems(commandsObservableList);

            BaseDao<CommandDish, Integer> commandsDishesDao = initCommandDishDao();
            List<CommandDish> foundCommandsDish = commandsDishesDao.findAll();
            foundCommandsDish.stream().forEach(commandDish -> {
                String dishName = commandDish.getDish().getName();
                int quantity = commandDish.getQuantity();
                float price = commandDish.getDish().getPrice();

                CommandList.setOnMouseClicked(event -> {
                    handleCommandClicked(dishName, quantity, price);
                });
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadCommandsFinish() {
        finishedCommandsObservableList.clear();

        List<Table> tablesInfo = getTablesInfos();
        List<Integer> tablesWithClients = tablesInfo.stream()
                .filter(table -> table.getPeopleLength() > 0)
                .map(table -> table.getId())
                .toList();

        try {
            BaseDao<Commands, Integer> commandsDao = initCommandDao();
            List<Commands> foundCommands = commandsDao.findAll();
            finishedCommandsObservableList.addAll(foundCommands.stream()
                    .filter(command -> command.getStatus().equals("Terminé"))
                    .filter(command -> tablesWithClients.contains(command.getIdTable())) // On ne garde que les commandes terminées des tables avec encore clients dessus (qui n'ont pas encore payé)
                    .toList());
            CommandFinishList.setItems(finishedCommandsObservableList);

            BaseDao<CommandDish, Integer> commandsDishesDao = initCommandDishDao();
            List<CommandDish> foundCommandsDish = commandsDishesDao.findAll();
            foundCommandsDish.stream().forEach(commandDish -> {
                        String dishName = commandDish.getDish().getName();
                        int quantity = commandDish.getQuantity();
                        float price = commandDish.getDish().getPrice();

                    CommandFinishList.setOnMouseClicked(event -> {
                        handleCommandClicked(dishName, quantity, price);
                    });
            });




        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private void updateCommandStatus(Commands command, String newStatus) {
        try {
            BaseDao<Commands, Integer> commandsDao = initCommandDao();
            command.setStatus(newStatus);
            commandsDao.update(command);
            loadCommands();
            loadCommandsFinish();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleCommandClicked(String dishName, int quantity, float price) {

        Commands selectedCommand = CommandList.getSelectionModel().getSelectedItem();
        Commands selectedCommandFInish = CommandFinishList.getSelectionModel().getSelectedItem();

        VBox detailsPanel = new VBox(10);
        detailsPanel.setStyle("-fx-background-color: lightblue;");
        detailsPanel.setPadding(new Insets(10));

        Stage commandDetails = new Stage();
        commandDetails.setTitle("Détails de la commande");

        Label dishLabel = new Label("Plat: " + dishName);
        dishLabel.setStyle("-fx-font-size: 20px;");
        Label quantityLabel = new Label("Quantité : " + quantity);
        quantityLabel.setStyle("-fx-font-size: 20px;");
        Label priceLabel = new Label("Prix : " + price);
        priceLabel.setStyle("-fx-font-size: 20px;");

        detailsPanel.getChildren().addAll(dishLabel, quantityLabel, priceLabel);

        Scene scene = new Scene(detailsPanel, 400, 160);

        commandDetails.setScene(scene);
        commandDetails.show();

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