package org.example.thegreatests.Controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.example.thegreatests.Models.BaseDao;
import org.example.thegreatests.Models.People;
import org.example.thegreatests.Models.Table;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TablesController {
    @FXML
    private GridPane gridPane;

    @FXML
    private Pane paneTable;

    @FXML
    private Pane paneTable2;

    @FXML
    private Pane paneClientTable;

    @FXML
    private VBox vBoxClientTable;


    @FXML
    private Label paneTableName;

    @FXML
    private TextField textFieldTableSize;

    @FXML
    private TextField textFieldTableLocation;

    @FXML
    private TextField textFieldTableSizeAdd;

    @FXML
    private TextField textFieldTableLocationAdd;

    @FXML
    private TextField textFieldNbrClientsAdding;

    private Integer currentTableId;

    @FXML
    public void initialize() {
        System.out.println("Controller Table");

        List<Table> foundTable = getTableInfos();

        showTable(foundTable);

        System.out.println("Table trouvée "+foundTable);

    }

    @FXML
    private void updateTableInfos() {
        System.out.println("J'ai cliqué eeeeeeeeeeeeeeeeee");
    }

    private List<Table> getTableInfos() {
        System.out.println("J'ai cliqué sur le bouton");
        try {
            BaseDao<Table, Integer> tableDao = initTableDao();
            List<Table> foundtable = tableDao.findAll();

            foundtable.stream().filter(t -> t.getStatus().equals("Libre")).collect(Collectors.toList());
            return foundtable;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void reloadtable() {
        gridPane.getChildren().retainAll(gridPane.getChildren().get(0));
        List<Table> foundTable = getTableInfos();
        showTable(foundTable);
    }

    private void showTable(List<Table> foundTable) {
        foundTable.stream()
                .forEach(
                        t -> {
                            VBox tablePane = new VBox();
                            Label label = new Label("Table "+ t.getLocation() +" - id"+ t.getId() + " (" + t.getStatus() + ")\nCapacité " + t.getSize());
                            Button buttonUpdate = new Button("Modifier");
                            buttonUpdate.setOnAction(event -> {
                                System.out.println("J'ai cliqué sur la table " + t.getId());
                                currentTableId = t.getId();
                                openTablePanel(t);
                            });
                            tablePane.getChildren().addAll(label, buttonUpdate);
                            gridPane.add(tablePane, foundTable.indexOf(t)%5, foundTable.indexOf(t)/5);
                        }
                );
    }

    private void openTablePanel(Table t) {
        paneTableName.setText("Table "+t.getLocation());
        paneTable.setVisible(true);
        textFieldTableSize.setText(t.getSize()+"");
        textFieldTableLocation.setText(t.getLocation());
        if (vBoxClientTable != null) {
            vBoxClientTable.getChildren().clear();
        }
        System.out.println("Liste de Peoples "+t.getPeopleList());
        // Récupérer tous les clients de la table et les ajouter à la vBox
        try {
            BaseDao<Table, Integer> tableDao = initTableDao();
            Table table = tableDao.findById(currentTableId);
            List<People> peopleList = table.getPeopleList();
            if (peopleList != null) {
                peopleList.stream().forEach(p -> {
                    Label label = new Label("Client "+p.getId());
                    vBoxClientTable.getChildren().add(label);
                });
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @FXML
    private void openCreateTablePanel() {
        paneTableName.setText("Ajouter une table");
        paneTable2.setVisible(true);
        textFieldTableSizeAdd.setText("");
        textFieldTableLocationAdd.setText("");
    }

    @FXML
    private void closeTablePanel() {
        paneTable.setVisible(false);
        paneTable2.setVisible(false);
        paneClientTable.setVisible(false);
    }

    @FXML
    private void deleteTable() {
        System.out.println("J'ai cliqué sur le bouton supprimer");
        try {
            BaseDao<Table, Integer> tableDao = initTableDao();
            tableDao.deleteById(currentTableId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        closeTablePanel();
        reloadtable();
    }

    @FXML
    private void createTable() {
        System.out.println("J'ai cliqué sur le bouton ajouter");
        try {

            BaseDao<Table, Integer> tableDao = initTableDao();

            Table table = new Table("Libre", Integer.parseInt(textFieldTableSizeAdd.getText()), textFieldTableLocationAdd.getText());
            tableDao.create(table);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        closeTablePanel();
        reloadtable();
    }

    @FXML
    private void saveTable() {
        System.out.println("J'ai cliqué sur le bouton sauvegarder");
        try {
            BaseDao<Table, Integer> tableDao = initTableDao();
            Table table = tableDao.findById(currentTableId);
            table.setSize(Integer.parseInt(textFieldTableSize.getText()));
            table.setLocation(textFieldTableLocation.getText());
            tableDao.update(table);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        closeTablePanel();
        reloadtable();
    }

    private BaseDao<Table, Integer> initTableDao() {
        try {
            String url = "jdbc:sqlite:database.db";
            JdbcConnectionSource source = new JdbcConnectionSource(url);
            TableUtils.createTableIfNotExists(source, Table.class);
            BaseDao<Table, Integer> tableDao = new BaseDao<>(source, Table.class);
            return tableDao;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void clearTable() {
        System.out.println("J'ai cliqué sur le bouton vider la table");
        try {
            BaseDao<Table, Integer> tableDao = initTableDao();
            Table table = tableDao.findById(currentTableId);
            table.setPeopleList(null);
            table.setStatus("Libre");
            tableDao.update(table);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        closeTablePanel();
        reloadtable();
    }

    @FXML
    private void openClientTable()
    {
        paneClientTable.setVisible(true);
    }

    @FXML
    private void addClientsToTable() {
        try {
            BaseDao<Table, Integer> tableDao = initTableDao();
            Table table = tableDao.findById(currentTableId);
            List<People> peopleList = table.getPeopleList();
            if (peopleList != null) {
                peopleList = new ArrayList<>(peopleList);
            }
            int n = Integer.parseInt(textFieldNbrClientsAdding.getText());
            // Je vais le modifier plus tard oui j'ai compris
            for (int i = 0; i < n; i++) {
                peopleList.add(new People(0));
            }
            table.setPeopleList(peopleList);
            table.setStatus("Occupée");
            tableDao.update(table);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        closeTablePanel();
        reloadtable();
    }
}