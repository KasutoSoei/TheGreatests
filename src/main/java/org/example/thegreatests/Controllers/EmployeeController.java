package org.example.thegreatests.Controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.thegreatests.Models.BaseDao;
import org.example.thegreatests.Models.Employees;

import java.io.IOException;
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
    private Pane pane;

    /**
     * This method is used to initialize the EmployeeController.
     */
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

    /**
     * This method is used to display the employees in the employee table.
     */
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

    /**
     * This method is used to reload the employee table.
     */
    private void reloadEmployee() {

        employeeGridPane.getChildren().retainAll(employeeGridPane.getChildren().get(0));
        List<Employees> foundEmployee = getEmployeeInfo();
        showEmployeeTable(foundEmployee);
    }

    /**
     * This method is used to create a new employee.
     */
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

    /**
     * This method is used to delete an employee.
     */
    @FXML
    private void deleteEmployee() {

        try {
            BaseDao<Employees, Integer> employeeDao = initEmployeeDao();
            employeeDao.deleteById(currentEmployeeId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        closeEmployeePanel();
        reloadEmployee();
    }

    /**
     * This method is used to update an employee.
     */
    @FXML
    private void updateEmployee() {

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

    /**
     * This method is used to update the worked hours of an employee.
     */
    @FXML
    private void updateEmployeeHoures(){

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

    /**
     * This method is used to close the employee panel.
     */
    @FXML
    private void closeEmployeePanel() {

        paneEmployee.setVisible(false);
        paneEmployeeCreateEmployee.setVisible(false);
        paneEmployeeHoure.setVisible(false);
    }

    /**
     * This method is used to open the employee panel.
     * @param e The employee to display.
     */
    private void openEmployeePanel(Employees e) {

        paneEmployee.setVisible(true);
        nameTextField.setText(e.getName());
        postTextField.setText(e.getJob());
        houreEmployeeLabel.setText(e.getWorkedHours()+"h");
    }

    /**
     * This method is used to open the add employee hours panel.
     */
    @FXML
    private void openAddEmployeeHoures() {

        paneEmployeeHoure.setVisible(true);
    }

    /**
     * This method is used to open the create employee panel.
     */
    public void openCreateEmployeePanel() {

        paneEmployeeCreateEmployee.setVisible(true);
    }

    /**
     * This function is used to get the employee information from the database.
     * @return List<Employees> The list of employees.
     */
    public List<Employees> getEmployeeInfo() {

        try {
            BaseDao<Employees, Integer> employeesDao = initEmployeeDao();
            return employeesDao.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This function is used to initialize the employee DAO.
     * @return BaseDao<Employees, Integer> The employee DAO.
     */
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
