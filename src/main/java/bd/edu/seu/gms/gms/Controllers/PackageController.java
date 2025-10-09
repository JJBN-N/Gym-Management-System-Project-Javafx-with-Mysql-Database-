package bd.edu.seu.gms.gms.Controllers;

import bd.edu.seu.gms.gms.HelloApplication;
import bd.edu.seu.gms.gms.Models.Package;
import bd.edu.seu.gms.gms.Services.PackageService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PackageController implements Initializable {

    @FXML public TableView<Package> packagesTable;
    @FXML public TableColumn<Package, String> nameColumn;
    @FXML public TableColumn<Package, String> typeColumn;
    @FXML public TableColumn<Package, Integer> durationColumn;
    @FXML public TableColumn<Package, Double> priceColumn;
    @FXML public TableColumn<Package, String> statusColumn;

    @FXML public TextField nameField;
    @FXML public ComboBox<String> typeField;
    @FXML public TextField durationField;
    @FXML public TextField priceField;
    @FXML public TextArea descriptionField;
    @FXML public TextArea featuresField;
    @FXML public ComboBox<String> statusField;

    @FXML public Button addButton;
    @FXML public Button updateButton;
    @FXML public Button deleteButton;
    @FXML public Button clearButton;

    public PackageService packageService;
    public ObservableList<Package> packagesList;
    public Package selectedPackage;

    public PackageController() {
        packageService = new PackageService();
        packagesList = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        loadPackages();
        setupForm();
    }

    public void setupTableColumns() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("durationDays"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        packagesTable.setItems(packagesList);

        packagesTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selectPackage(newValue));
    }

    public void setupForm() {
        typeField.getItems().addAll("Basic", "Premium", "Custom");
        statusField.getItems().addAll("active", "inactive");

        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    public void loadPackages() {
        packagesList.clear();
        List<Package> packages = packageService.getAllPackages();
        packagesList.addAll(packages);
    }

    public void selectPackage(Package packageObj) {
        if (packageObj != null) {
            selectedPackage = packageObj;
            nameField.setText(packageObj.getName());
            typeField.setValue(packageObj.getType());
            durationField.setText(String.valueOf(packageObj.getDurationDays()));
            priceField.setText(String.valueOf(packageObj.getPrice()));
            descriptionField.setText(packageObj.getDescription());
            featuresField.setText(packageObj.getFeatures());
            statusField.setValue(packageObj.getStatus());

            updateButton.setDisable(false);
            deleteButton.setDisable(false);
            addButton.setDisable(true);
        }
    }

    @FXML
    public void addPackageEvent(ActionEvent event) {
        if (validateForm()) {
            Package packageObj = new Package();
            packageObj.setName(nameField.getText());
            packageObj.setType(typeField.getValue());
            packageObj.setDurationDays(Integer.parseInt(durationField.getText()));
            packageObj.setPrice(Double.parseDouble(priceField.getText()));
            packageObj.setDescription(descriptionField.getText());
            packageObj.setFeatures(featuresField.getText());
            packageObj.setStatus(statusField.getValue());

            if (packageService.addPackage(packageObj)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Package added successfully!");
                clearForm();
                loadPackages();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add package.");
            }
        }
    }

    @FXML
    public void updatePackageEvent(ActionEvent event) {
        if (selectedPackage != null && validateForm()) {
            selectedPackage.setName(nameField.getText());
            selectedPackage.setType(typeField.getValue());
            selectedPackage.setDurationDays(Integer.parseInt(durationField.getText()));
            selectedPackage.setPrice(Double.parseDouble(priceField.getText()));
            selectedPackage.setDescription(descriptionField.getText());
            selectedPackage.setFeatures(featuresField.getText());
            selectedPackage.setStatus(statusField.getValue());

            if (packageService.updatePackage(selectedPackage)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Package updated successfully!");
                clearForm();
                loadPackages();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update package.");
            }
        }
    }

    @FXML
    public void deletePackageEvent(ActionEvent event) {
        if (selectedPackage != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Delete Package");
            alert.setContentText("Are you sure you want to delete " + selectedPackage.getName() + "?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                if (packageService.deletePackage(selectedPackage.getId())) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Package deleted successfully!");
                    clearForm();
                    loadPackages();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete package.");
                }
            }
        }
    }

    @FXML
    public void clearFormEvent(ActionEvent event) {
        clearForm();
    }

    @FXML
    public void backToDashboardEvent(ActionEvent event) {
        HelloApplication.changeScene("dashboard");
    }

    public void clearForm() {
        nameField.clear();
        typeField.setValue(null);
        durationField.clear();
        priceField.clear();
        descriptionField.clear();
        featuresField.clear();
        statusField.setValue("active");

        packagesTable.getSelectionModel().clearSelection();
        selectedPackage = null;

        addButton.setDisable(false);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    public boolean validateForm() {
        if (nameField.getText().isEmpty() || typeField.getValue() == null ||
                durationField.getText().isEmpty() || priceField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
            return false;
        }

        try {
            Integer.parseInt(durationField.getText());
            Double.parseDouble(priceField.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter valid numbers for duration and price.");
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
