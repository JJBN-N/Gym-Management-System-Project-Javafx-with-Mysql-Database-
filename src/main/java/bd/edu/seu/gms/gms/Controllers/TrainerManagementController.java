package bd.edu.seu.gms.gms.Controllers;

import bd.edu.seu.gms.gms.HelloApplication;
import bd.edu.seu.gms.gms.Models.Trainer;
import bd.edu.seu.gms.gms.Models.User;
import bd.edu.seu.gms.gms.Services.TrainerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class TrainerManagementController implements Initializable {

    @FXML
    public TableView<Trainer> trainersTable;

    @FXML
    public TableColumn<Trainer, String> nameColumn;

    @FXML
    public TableColumn<Trainer, String> specializationColumn;

    @FXML
    public TableColumn<Trainer, Integer> experienceColumn;

    @FXML
    public TableColumn<Trainer, String> statusColumn;

    @FXML
    public TextField nameField;

    @FXML
    public TextField usernameField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public TextField emailField;

    @FXML
    public TextField phoneField;

    @FXML
    public TextField specializationField;

    @FXML
    public TextField experienceField;

    @FXML
    public TextField salaryField;

    @FXML
    public ComboBox<String> statusComboBox;

    public TrainerService trainerService;
    public ObservableList<Trainer> trainersList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        trainerService = new TrainerService();
        trainersList = FXCollections.observableArrayList();

        setupTable();
        loadTrainers();
        setupComboBox();
    }

    public void setupTable() {
        nameColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUser().getName()));
        specializationColumn.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        experienceColumn.setCellValueFactory(new PropertyValueFactory<>("experienceYears"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        trainersTable.setItems(trainersList);
    }

    public void loadTrainers() {
        trainersList.clear();
        trainersList.addAll(trainerService.getAllTrainers());
    }

    public void setupComboBox() {
        statusComboBox.getItems().addAll("active", "inactive");
        statusComboBox.setValue("active");
    }

    @FXML
    public void addTrainerEvent(ActionEvent event) {
        if (validateInput()) {
            Trainer trainer = new Trainer();
            trainer.setSpecialization(specializationField.getText());
            trainer.setExperienceYears(Integer.parseInt(experienceField.getText()));
            trainer.setSalary(Double.parseDouble(salaryField.getText()));
            trainer.setHireDate(LocalDate.now());
            trainer.setStatus(statusComboBox.getValue());

            User user = new User();
            user.setUsername(usernameField.getText());
            user.setPassword(passwordField.getText());
            user.setName(nameField.getText());
            user.setEmail(emailField.getText());
            user.setPhone(phoneField.getText());

            trainer.setUser(user);

            if (trainerService.addTrainer(trainer)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Trainer added successfully");
                clearForm();
                loadTrainers();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add trainer");
            }
        }
    }

    @FXML
    public void updateTrainerEvent(ActionEvent event) {
        Trainer selectedTrainer = trainersTable.getSelectionModel().getSelectedItem();
        if (selectedTrainer != null) {
            // Implementation for updating trainer
            showAlert(Alert.AlertType.INFORMATION, "Update", "Update functionality to be implemented");
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a trainer to update");
        }
    }

    @FXML
    public void deleteTrainerEvent(ActionEvent event) {
        Trainer selectedTrainer = trainersTable.getSelectionModel().getSelectedItem();
        if (selectedTrainer != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirm Delete");
            confirmation.setHeaderText("Delete Trainer");
            confirmation.setContentText("Are you sure you want to delete " + selectedTrainer.getUser().getName() + "?");

            if (confirmation.showAndWait().get() == ButtonType.OK) {
                if (trainerService.deleteTrainer(selectedTrainer.getId())) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Trainer deleted successfully");
                    loadTrainers();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete trainer");
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a trainer to delete");
        }
    }

    public boolean validateInput() {
        if (nameField.getText().isEmpty() || usernameField.getText().isEmpty() ||
                passwordField.getText().isEmpty() || specializationField.getText().isEmpty() ||
                experienceField.getText().isEmpty() || salaryField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill all required fields");
            return false;
        }

        try {
            Integer.parseInt(experienceField.getText());
            Double.parseDouble(salaryField.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Experience and Salary must be valid numbers");
            return false;
        }

        return true;
    }

    public void clearForm() {
        nameField.clear();
        usernameField.clear();
        passwordField.clear();
        emailField.clear();
        phoneField.clear();
        specializationField.clear();
        experienceField.clear();
        salaryField.clear();
        statusComboBox.setValue("active");
    }

    @FXML
    public void backToDashboard(ActionEvent event) {
        HelloApplication.changeScene("dashboard");
    }

    public void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}