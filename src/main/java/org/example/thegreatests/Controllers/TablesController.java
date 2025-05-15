package org.example.thegreatests.Controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.thegreatests.Models.BaseDao;
import org.example.thegreatests.Models.Table;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

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
    private Label labelClientNumber;

    @FXML
    private Label labelErrorTable;

    @FXML
    private Label labelErrorAddClient;

    @FXML
    private Label labelErrorCreateTable;

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

    @FXML
    private Button buttonCreateCommand;

    private Integer currentTableId;

    @FXML
    private Pane pane;

    /**
     * This method is used to initialize the TablesController.
     */
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

    /**
     * This function is used to get the tables information from the database.
     * @return List<Table> The list of tables.
     */
    private List<Table> getTableInfos() {

        System.out.println("J'ai cliqué sur le bouton");
        try {
            BaseDao<Table, Integer> tableDao = initTableDao();
            List<Table> foundtable = tableDao.findAll();

            // foundtable = foundtable.stream().filter(t -> t.getStatus().equals("Libre")).collect(Collectors.toList());
            return foundtable;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is used to reload the table information.
     */
    private void reloadtable() {

        gridPane.getChildren().retainAll(gridPane.getChildren().get(0));
        List<Table> foundTable = getTableInfos();
        showTable(foundTable);
    }

    /**
     * This method is used to display the tables in the grid pane.
     * @param foundTable The list of tables to display.
     */
    private void showTable(List<Table> foundTable) {

        foundTable.stream()
                .forEach(
                        t -> {
                            VBox tablePane = new VBox();
                            Label label = new Label("Table "+ t.getLocation() +" - id"+ t.getId() + " (" + t.getStatus() + ")\nCapacité " + t.getSize());
                            if (t.getPeopleLenght() > 0) {
                                tablePane.setStyle("-fx-background-color: lightgray");
                            }
                            Button buttonUpdate = new Button("Modifier");
                            buttonUpdate.setOnAction(event -> {
                                System.out.println("J'ai cliqué sur la table " + t.getId());
                                currentTableId = t.getId();
                                openTablePanel(t);
                            });
                            tablePane.getChildren().addAll(label, buttonUpdate);
                            tablePane.alignmentProperty().setValue(javafx.geometry.Pos.CENTER);
                            gridPane.add(tablePane, foundTable.indexOf(t)%5, foundTable.indexOf(t)/5);
                        }
                );
    }

    /**
     * This method is used to open the table panel.
     * @param t The table to open.
     */
    private void openTablePanel(Table t) {

        labelErrorTable.setText("");
        paneTableName.setText("Table "+t.getLocation());
        paneTable.setVisible(true);
        textFieldTableSize.setText(t.getSize()+"");
        textFieldTableLocation.setText(t.getLocation());
        System.out.println("Liste de Peoples "+t.getPeopleLenght());
            if (t.getPeopleLenght() > 0) {
                buttonCreateCommand.setDisable(false);
                labelClientNumber.setText("Nombre de clients : " + t.getPeopleLenght());
            } else {
                buttonCreateCommand.setDisable(true);
                labelClientNumber.setText("Aucun client");
            }
    }

    /**
     * This method is used to open the create table panel.
     */
    @FXML
    private void openCreateTablePanel() {

        paneTableName.setText("Ajouter une table");
        paneTable2.setVisible(true);
        textFieldTableSizeAdd.setText("");
        textFieldTableLocationAdd.setText("");
        labelErrorCreateTable.setText("");
    }

    /**
     * This method is used to close the table panel.
     */
    @FXML
    private void closeTablePanel() {

        paneTable.setVisible(false);
        paneTable2.setVisible(false);
        paneClientTable.setVisible(false);
    }

    /**
     * This method is used to delete a table.
     */
    @FXML
    private void deleteTable() {

        try {
            BaseDao<Table, Integer> tableDao = initTableDao();
            tableDao.deleteById(currentTableId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        closeTablePanel();
        reloadtable();
    }

    /**
     * This method is used to create a table.
     */
    @FXML
    private void createTable() {

        try {

            BaseDao<Table, Integer> tableDao = initTableDao();
            try {
                Table table = new Table(Integer.parseInt(textFieldTableSizeAdd.getText()), textFieldTableLocationAdd.getText());
                tableDao.create(table);
            } catch (NumberFormatException e) {
                labelErrorCreateTable.setText("Erreur : Veuillez entrer un nombre valide");
                return;
            }
        } catch (SQLException e) {
            labelErrorCreateTable.setText("Erreur : "+e.getMessage());
            return;
        }
        closeTablePanel();
        reloadtable();
    }

    /**
     * This method is used to save the table information.
     */
    @FXML
    private void saveTable() {

        try {
            BaseDao<Table, Integer> tableDao = initTableDao();
            Table table = tableDao.findById(currentTableId);
            try {
                table.setSize(Integer.parseInt(textFieldTableSize.getText()));
            } catch (NumberFormatException e) {
                labelErrorTable.setText("Erreur : Veuillez entrer un nombre valide");
                return;
            }
            table.setLocation(textFieldTableLocation.getText());
            tableDao.update(table);
        } catch (SQLException e) {
            labelErrorTable.setText("Erreur : "+e.getMessage());
            return;
        }
        closeTablePanel();
        reloadtable();
    }

    /**
     * This method is used to change the scene.
     */
    public void handleChangeScene(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/org/example/thegreatests/Employee-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This function is used to initialize the table DAO.
     * @return BaseDao<Table, Integer> The table DAO.
     */
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

    /**
     * This method is used to clear the table.
     */
    @FXML
    private void clearTable() {

        try {
            BaseDao<Table, Integer> tableDao = initTableDao();
            Table table = tableDao.findById(currentTableId);
            table.setPeopleLenght(0);
            tableDao.update(table);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        closeTablePanel();
        reloadtable();
    }

    /**
     * This method is used to initialize the CommandsController.
     */
    @FXML
    private void openClientTable()
    {

        labelErrorAddClient.setText("");
        paneClientTable.setVisible(true);
    }

    /**
     * This method is used to add clients to a table.
     */
    @FXML
    private void addClientsToTable() {

        try {
            BaseDao<Table, Integer> tableDao = initTableDao();
            Table table = tableDao.findById(currentTableId);
            Integer peopleList = table.getPeopleLenght();
            System.out.println("Liste de Peoples "+peopleList);
            if (peopleList >= table.getSize()) {
                labelErrorAddClient.setText("Attention : La table est déjà pleine");
                return;
            } else {
                try {
                    peopleList += Integer.parseInt(textFieldNbrClientsAdding.getText());
                    table.setPeopleLenght(peopleList);
                    tableDao.update(table);
                } catch (NumberFormatException e) {
                    labelErrorAddClient.setText("Erreur : Veuillez entrer un nombre valide");
                    return;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        closeTablePanel();
        reloadtable();
    }

    /**
     * This method is used to create a command.
     */
    @FXML
    private void onCreateCommand() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/thegreatests/adding-command-view.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur de la nouvelle vue
            Object controller = loader.getController();
            if (controller instanceof org.example.thegreatests.Controllers.AddingCommandController) {
                ((org.example.thegreatests.Controllers.AddingCommandController) controller).initData(currentTableId);
            }

            Stage stage = (Stage) paneTable.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
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