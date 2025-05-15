package org.example.thegreatests.Controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.thegreatests.Models.BaseDao;
import org.example.thegreatests.Models.CommandDish;
import org.example.thegreatests.Models.Commands;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CommandsController {

    @FXML
    private ListView<HBox> CommandList;

    @FXML
    private ListView<HBox> CommandFinishList;

    @FXML
    private Pane pane;

    /**
     * This method is used to initialize the CommandsController.
     */
    @FXML
    public void initialize() {
        loadCommands();
        loadCommandsFinish();
    }


    /**
     * This function is used to initialize the commands DAO.
     * @return BaseDao<Commands, Integer> The commands DAO.
     */
    private BaseDao<Commands, Integer> initCommandDishDao() {
        try {
            String url = "jdbc:sqlite:database.db";
            JdbcConnectionSource source = new JdbcConnectionSource(url);
            TableUtils.createTableIfNotExists(source, Commands.class);
            BaseDao<Commands, Integer> CommandsDao = new BaseDao<>(source, Commands.class);
            return CommandsDao;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is used to load the commands from the database and display them in the ListView.
     */
    private void loadCommands() {
        CommandList.getItems().clear();

        try {
            BaseDao<Commands, Integer> CommandsDao = initCommandDishDao();

            List<Commands> foundCommands = CommandsDao.findAll();

            foundCommands.stream().filter(command -> command.getStatus().equals("En attente")).forEach(command -> {
                Label label = new Label("Commande n°" + command.getId() + " - " + command.getStatus() + " - " + command.getDate());
                label.setStyle("-fx-font-size: 15px;");

                Button deleteBtn = new Button("Annuler");
                if (command.getStatus().equals("Terminé")) {
                    deleteBtn.setVisible(false);
                }
                else {
                    deleteBtn.setOnAction(e -> {
                        try {
                            command.setStatus("Annulée");
                            CommandsDao.update(command);
                            CommandList.getItems().clear();
                            loadCommands();
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                }
                Button doneBtn = new Button("Terminer");
                if (command.getStatus().equals("Terminé")) {
                    doneBtn.setVisible(false);
                }
                else {
                    doneBtn.setOnAction(e -> {
                        try {
                            command.setStatus("Terminé");
                            CommandsDao.update(command);
                            CommandList.getItems().clear();
                            CommandFinishList.getItems().clear();
                            loadCommandsFinish();
                            loadCommands();
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                }

                VBox labelContainer = new VBox(label);
                labelContainer.setAlignment(Pos.CENTER_LEFT);
                VBox deleteBtnContainer = new VBox(deleteBtn);
                deleteBtnContainer.setAlignment(Pos.CENTER);

                HBox hbox = new HBox(10, labelContainer, doneBtn, deleteBtnContainer);
                hbox.setPadding(new Insets(5));
                hbox.setAlignment(Pos.CENTER_LEFT);

                CommandList.getItems().add(hbox);
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadCommandsFinish () {
        BaseDao<Commands, Integer> CommandsDao = initCommandDishDao();

        try{
        List<Commands> foundCommands = CommandsDao.findAll();

        foundCommands.stream().limit(6).filter(command -> command.getStatus().equals("Terminé")).forEach(command -> {
            Label label = new Label("Commande n°" + command.getId() + " - " + command.getStatus() + " - " + command.getDate());
            label.setStyle("-fx-font-size: 15px;");

            VBox labelContainer = new VBox(label);
            labelContainer.setAlignment(Pos.CENTER_LEFT);

            HBox hbox = new HBox(10, labelContainer);
            hbox.setPadding(new Insets(5));
            hbox.setAlignment(Pos.CENTER_LEFT);

            CommandFinishList.getItems().add(hbox);
        });

    } catch (SQLException e) {
        e.printStackTrace();
    }
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