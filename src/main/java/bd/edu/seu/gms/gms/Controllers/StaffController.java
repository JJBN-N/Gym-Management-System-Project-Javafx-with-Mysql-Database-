package bd.edu.seu.gms.gms.Controllers;

import bd.edu.seu.gms.gms.HelloApplication;
import bd.edu.seu.gms.gms.Models.Staff;
import bd.edu.seu.gms.gms.Services.StaffService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class StaffController implements Initializable {

    @FXML public TableView<Staff> staffTable;
    @FXML public TableColumn<Staff, String> idColumn;
    @FXML public TableColumn<Staff, String> nameColumn;
    @FXML public TableColumn<Staff, String> positionColumn;
    @FXML public TableColumn<Staff, String> phoneColumn;
    @FXML public TableColumn<Staff, String> statusColumn;

    @FXML public TextField searchField;
    @FXML public TextField staffIdField;
    @FXML public TextField nameField;
    @FXML public TextField emailField;
    @FXML public TextField phoneField;
    @FXML public TextArea addressField;
    @FXML public DatePicker dobField;
    @FXML public ComboBox<String> genderField;
    @FXML public DatePicker hireDateField;
    @FXML public TextField positionField;
    @FXML public TextField specializationField;
    @FXML public TextField salaryField;
    @FXML public ComboBox<String> statusField;

    @FXML public Button addButton;
    @FXML public Button updateButton;
    @FXML public Button deleteButton;
    @FXML public Button clearButton;

    public StaffService staffService;
    public ObservableList<Staff> staffList;
    public Staff selectedStaff;

    public StaffController() {
        staffService = new StaffService();
        staffList = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        loadStaff();
        setupForm();
    }

    public void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("staffId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        staffTable.setItems(staffList);

        staffTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selectStaff(newValue));
    }

    public void setupForm() {
        genderField.getItems().addAll("Male", "Female", "Other");
        statusField.getItems().addAll("active", "inactive");

        hireDateField.setValue(LocalDate.now());

        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    public void loadStaff() {
        staffList.clear();
        List<Staff> staff = staffService.getAllStaff();
        staffList.addAll(staff);
    }

    public void selectStaff(Staff staff) {
        if (staff != null) {
            selectedStaff = staff;
            staffIdField.setText(staff.getStaffId());
            nameField.setText(staff.getName());
            emailField.setText(staff.getEmail());
            phoneField.setText(staff.getPhone());
            addressField.setText(staff.getAddress());
            dobField.setValue(staff.getDateOfBirth());
            genderField.setValue(staff.getGender());
            hireDateField.setValue(staff.getHireDate());
            positionField.setText(staff.getPosition());
            specializationField.setText(staff.getSpecialization());
            salaryField.setText(String.valueOf(staff.getSalary()));
            statusField.setValue(staff.getStatus());

            updateButton.setDisable(false);
            deleteButton.setDisable(false);
            addButton.setDisable(true);
        }
    }

    @FXML
    public void addStaffEvent(ActionEvent event) {
        if (validateForm()) {
            Staff staff = new Staff();
            staff.setStaffId(staffIdField.getText());
            staff.setName(nameField.getText());
            staff.setEmail(emailField.getText());
            staff.setPhone(phoneField.getText());
            staff.setAddress(addressField.getText());
            staff.setDateOfBirth(dobField.getValue());
            staff.setGender(genderField.getValue());
            staff.setHireDate(hireDateField.getValue());
            staff.setPosition(positionField.getText());
            staff.setSpecialization(specializationField.getText());
            staff.setSalary(Double.parseDouble(salaryField.getText()));
            staff.setStatus(statusField.getValue());

            if (staffService.addStaff(staff)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Staff added successfully!");
                clearForm();
                loadStaff();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add staff.");
            }
        }
    }

    @FXML
    public void updateStaffEvent(ActionEvent event) {
        if (selectedStaff != null && validateForm()) {
            selectedStaff.setName(nameField.getText());
            selectedStaff.setEmail(emailField.getText());
            selectedStaff.setPhone(phoneField.getText());
            selectedStaff.setAddress(addressField.getText());
            selectedStaff.setDateOfBirth(dobField.getValue());
            selectedStaff.setGender(genderField.getValue());
            selectedStaff.setHireDate(hireDateField.getValue());
            selectedStaff.setPosition(positionField.getText());
            selectedStaff.setSpecialization(specializationField.getText());
            selectedStaff.setSalary(Double.parseDouble(salaryField.getText()));
            selectedStaff.setStatus(statusField.getValue());

            if (staffService.updateStaff(selectedStaff)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Staff updated successfully!");
                clearForm();
                loadStaff();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update staff.");
            }
        }
    }

    @FXML
    public void deleteStaffEvent(ActionEvent event) {
        if (selectedStaff != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Delete Staff");
            alert.setContentText("Are you sure you want to delete " + selectedStaff.getName() + "?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                if (staffService.deleteStaff(selectedStaff.getId())) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Staff deleted successfully!");
                    clearForm();
                    loadStaff();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete staff.");
                }
            }
        }
    }

    @FXML
    public void clearFormEvent(ActionEvent event) {
        clearForm();
    }

    @FXML
    public void searchEvent(ActionEvent event) {
        String keyword = searchField.getText();
        if (keyword.isEmpty()) {
            loadStaff();
        } else {
            staffList.clear();
            List<Staff> staff = staffService.searchStaff(keyword);
            staffList.addAll(staff);
        }
    }

    @FXML
    public void backToDashboardEvent(ActionEvent event) {
        HelloApplication.changeScene("dashboard");
    }

    public void clearForm() {
        staffIdField.clear();
        nameField.clear();
        emailField.clear();
        phoneField.clear();
        addressField.clear();
        dobField.setValue(null);
        genderField.setValue(null);
        hireDateField.setValue(LocalDate.now());
        positionField.clear();
        specializationField.clear();
        salaryField.clear();
        statusField.setValue("active");

        staffTable.getSelectionModel().clearSelection();
        selectedStaff = null;

        addButton.setDisable(false);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    public boolean validateForm() {
        if (staffIdField.getText().isEmpty() || nameField.getText().isEmpty() ||
                phoneField.getText().isEmpty() || hireDateField.getValue() == null ||
                positionField.getText().isEmpty() || salaryField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
            return false;
        }

        try {
            Double.parseDouble(salaryField.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid salary.");
            return false;
        }

        return true;
    }

    public void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}