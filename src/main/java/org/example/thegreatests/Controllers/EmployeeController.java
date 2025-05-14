package org.example.thegreatests.Controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.example.thegreatests.Models.BaseDao;
import org.example.thegreatests.Models.Employees;

import java.sql.SQLException;
import java.util.List;

public class EmployeeController {

    @FXML
    private GridPane employeeGridPane;

    @FXML
    private Pane paneEmployee;

    @FXML
    private Pane paneEmployeeCreateEmployee;

    @FXML
    private Pane paneEmployeeHoure;

    @FXML
    private TextField postTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField nameTextFieldAdd;

    @FXML
    private TextField postTextFieldAdd;

    @FXML
    private TextField textFieldHoures;

    @FXML
    private Label houreEmployeeLabel;

    @FXML
    private Integer currentEmployeeId;

    @FXML
    public void initialize() {
        List<Employees> foundEmployees = getEmployeeInfo();
        showEmployeeTable(foundEmployees);

        textFieldHoures.setTextFormatter(new TextFormatter<String>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        }));
    }

    public void showEmployeeTable(List<Employees> foundEmployees) {
        foundEmployees.stream()
                .forEach(
                        e -> {
                            VBox employeePlane = new VBox();
                            Label label = new Label("Nom :"+ e.getName() + "\nPoste :" + e.getJob() + "\nHeure travaillé :" + e.getWorkedHours() + "h");

                            Button buttonUpdate = new Button("Modifier");
                            buttonUpdate.setOnAction(event -> {
                                System.out.println("J'ai cliqué sur l'employés " + e.getName());
                                currentEmployeeId = e.getId();
                                openEmployeePanel(e);
                            });
                            employeePlane.getChildren().addAll(label, buttonUpdate);
                            employeePlane.alignmentProperty().setValue(javafx.geometry.Pos.CENTER);
                            employeeGridPane.add(employeePlane, foundEmployees.indexOf(e)%5, foundEmployees.indexOf(e)/5);
                        }
                );
    }

    private void reloadEmployee() {
        employeeGridPane.getChildren().retainAll(employeeGridPane.getChildren().get(0));
        List<Employees> foundEmployee = getEmployeeInfo();
        showEmployeeTable(foundEmployee);
    }

    @FXML
    private void createEmployee() {
        try{
            BaseDao<Employees, Integer> employeesDao = initEmployeeDao();
            Employees employees = new Employees(nameTextFieldAdd.getText(), postTextFieldAdd.getText());
            employeesDao.create(employees);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        closeEmployeePanel();
        reloadEmployee();
    }

    @FXML
    private void deleteEmployee() {
        System.out.println("J'ai cliqué sur le bouton supprimer");
        try {
            BaseDao<Employees, Integer> employeeDao = initEmployeeDao();
            employeeDao.deleteById(currentEmployeeId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        closeEmployeePanel();
        reloadEmployee();
    }

    @FXML
    private void updateEmployee() {
        System.out.println("J'ai cliqué sur le bouton sauvegarder");
        try {
            BaseDao<Employees, Integer> employeeDao = initEmployeeDao();
            Employees employees = employeeDao.findById(currentEmployeeId);
            employees.setJob(postTextField.getText());
            employees.setName(nameTextField.getText());
            employeeDao.update(employees);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        closeEmployeePanel();
        reloadEmployee();
    }

    @FXML
    private void updateEmployeeHoures(){
        System.out.println("J'ai cliqué sur le bouton sauvegarder");
        try {
            BaseDao<Employees, Integer> employeeDao = initEmployeeDao();
            Employees employees = employeeDao.findById(currentEmployeeId);
            employees.setWorkedHours(employees.getWorkedHours() + Integer.parseInt(textFieldHoures.getText()));
            employeeDao.update(employees);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        closeEmployeePanel();
        reloadEmployee();
    }

    @FXML
    private void closeEmployeePanel() {
        paneEmployee.setVisible(false);
        paneEmployeeCreateEmployee.setVisible(false);
        paneEmployeeHoure.setVisible(false);
    }

    private void openEmployeePanel(Employees e) {
        paneEmployee.setVisible(true);
        nameTextField.setText(e.getName());
        postTextField.setText(e.getJob());
        houreEmployeeLabel.setText(e.getWorkedHours()+"h");
    }

    @FXML
    private void openAddEmployeeHoures() {
        paneEmployeeHoure.setVisible(true);
    }

    public void openCreateEmployeePanel() {
        paneEmployeeCreateEmployee.setVisible(true);
    }

    public List<Employees> getEmployeeInfo() {
        try {
            BaseDao<Employees, Integer> employeesDao = initEmployeeDao();
            return employeesDao.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public BaseDao<Employees, Integer> initEmployeeDao() {
        try {
            String url = "jdbc:sqlite:database.db";
            JdbcConnectionSource source = new JdbcConnectionSource(url);
            TableUtils.createTableIfNotExists(source, Employees.class);
            return new BaseDao<>(source, Employees.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
