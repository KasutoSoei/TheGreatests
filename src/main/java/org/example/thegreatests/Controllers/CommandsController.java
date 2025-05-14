package org.example.thegreatests.Controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.thegreatests.Models.BaseDao;
import org.example.thegreatests.Models.Commands;

import java.sql.SQLException;
import java.util.List;

public class CommandsController {

    @FXML
    private ListView<HBox> MyListView;

    @FXML
    public void initialize() {
        loadCommands();
    }

    private void loadCommands() {
        MyListView.getItems().clear();
        try {
            String url = "jdbc:sqlite:database.db";
            JdbcConnectionSource source = new JdbcConnectionSource(url);

            TableUtils.createTableIfNotExists(source, Commands.class);
            BaseDao<Commands, Integer> tableDao = new BaseDao<>(source, Commands.class);

            List<Commands> foundCommands = tableDao.findAll();

            foundCommands.stream().filter(command -> !command.getStatus().equals("Annulée")).forEach(command -> {
                Label label = new Label("Commande n°" + command.getId() + " - " + command.getStatus() + " - " + command.getDate());
                label.setStyle("-fx-font-size: 20px;");

                Button deleteBtn = new Button("Annuler");
                if (command.getStatus().equals("Terminé")) {
                    deleteBtn.setVisible(false);
                }
                else {
                    deleteBtn.setOnAction(e -> {
                        try {
                            command.setStatus("Annulée");
                            tableDao.update(command);
                            MyListView.getItems().clear();
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
                            tableDao.update(command);
                            MyListView.getItems().clear();
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

                MyListView.getItems().add(hbox);
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}