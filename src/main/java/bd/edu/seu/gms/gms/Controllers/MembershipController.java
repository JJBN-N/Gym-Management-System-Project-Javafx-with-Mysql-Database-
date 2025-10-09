package bd.edu.seu.gms.gms.Controllers;

import bd.edu.seu.gms.gms.Models.Membership;
import bd.edu.seu.gms.gms.Models.Package;
import bd.edu.seu.gms.gms.Services.MembershipService;
import bd.edu.seu.gms.gms.Services.PackageService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class MembershipController implements Initializable {

    @FXML
    public TableView<Membership> membershipsTable;

    @FXML
    public TableColumn<Membership, String> membershipColumn;

    @FXML
    public TableColumn<Membership, String> packageNameColumn;

    @FXML
    public TableColumn<Membership, LocalDate> startDateColumn;

    @FXML
    public TableColumn<Membership, LocalDate> endDateColumn;

    @FXML
    public TableColumn<Membership, String> statusColumn;

    @FXML
    public TableColumn<Membership, Double> paidAmountColumn;

    @FXML
    public TableColumn<Membership, Double> dueAmountColumn;

    @FXML
    public TextField memberIdField;

    @FXML
    public ComboBox<Package> packageComboBox;

    @FXML
    public DatePicker startDateField;

    @FXML
    public DatePicker endDateField;

    @FXML
    public TextField paidAmountField;

    @FXML
    public TextField dueAmountField;

    @FXML
    public ComboBox<String> statusComboBox;

    @FXML
    public Button addButton;

    @FXML
    public Button updateButton;

    @FXML
    public Button deleteButton;

    @FXML
    public Button clearButton;

    public MembershipService membershipService;
    public PackageService packageService;
    public ObservableList<Membership> membershipsList;
    public ObservableList<Package> packagesList;
    public Membership selectedMembership;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        membershipService = new MembershipService();
        packageService = new PackageService();

        initializeTable();
        initializeForm();
        loadMemberships();
        loadPackages();
    }

    public void initializeTable() {
        // Initialize table columns
        membershipColumn.setCellValueFactory(new PropertyValueFactory<>("membershipId"));
        packageNameColumn.setCellValueFactory(new PropertyValueFactory<>("packageName"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        paidAmountColumn.setCellValueFactory(new PropertyValueFactory<>("paidAmount"));
        dueAmountColumn.setCellValueFactory(new PropertyValueFactory<>("dueAmount"));

        // Add listener for table selection
        membershipsTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selectMembership(newValue)
        );
    }

    public void initializeForm() {
        // Initialize status combobox
        statusComboBox.getItems().addAll("Active", "Expired", "Cancelled", "Pending");

        // Set default values
        startDateField.setValue(LocalDate.now());

        // Disable update and delete buttons initially
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    public void loadMemberships() {
        List<Membership> memberships = membershipService.getAllMemberships();
        membershipsList = FXCollections.observableArrayList(memberships);
        membershipsTable.setItems(membershipsList);
    }

    public void loadPackages() {
        List<Package> packages = packageService.getAllPackages();
        packagesList = FXCollections.observableArrayList(packages);
        packageComboBox.setItems(packagesList);
    }

    public void selectMembership(Membership membership) {
        if (membership != null) {
            selectedMembership = membership;
            memberIdField.setText(membership.getMemberId());

            // Find and set the package
            Package selectedPackage = packagesList.stream()
                    .filter(pkg -> pkg.getId() == membership.getPackageId())
                    .findFirst()
                    .orElse(null);
            packageComboBox.setValue(selectedPackage);

            startDateField.setValue(membership.getStartDate());
            endDateField.setValue(membership.getEndDate());
            paidAmountField.setText(String.valueOf(membership.getPaidAmount()));
            dueAmountField.setText(String.valueOf(membership.getDueAmount()));
            statusComboBox.setValue(membership.getStatus());

            // Enable update and delete buttons
            updateButton.setDisable(false);
            deleteButton.setDisable(false);
            addButton.setDisable(true);
        }
    }

    @FXML
    public void addMembershipEvent() {
        if (validateForm()) {
            Membership membership = new Membership();
            membership.setMemberId(memberIdField.getText());

            Package selectedPackage = packageComboBox.getValue();
            if (selectedPackage != null) {
                membership.setPackageId(selectedPackage.getId());
            }

            membership.setStartDate(startDateField.getValue());
            membership.setEndDate(endDateField.getValue());
            membership.setPaidAmount(Double.parseDouble(paidAmountField.getText()));
            membership.setDueAmount(Double.parseDouble(dueAmountField.getText()));
            membership.setStatus(statusComboBox.getValue());

            if (membershipService.createMembership(membership)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Membership added successfully!");
                clearForm();
                loadMemberships();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add membership.");
            }
        }
    }

    @FXML
    public void updateMembershipEvent() {
        if (selectedMembership != null && validateForm()) {
            selectedMembership.setMemberId(memberIdField.getText());

            Package selectedPackage = packageComboBox.getValue();
            if (selectedPackage != null) {
                selectedMembership.setPackageId(selectedPackage.getId());
            }

            selectedMembership.setStartDate(startDateField.getValue());
            selectedMembership.setEndDate(endDateField.getValue());
            selectedMembership.setPaidAmount(Double.parseDouble(paidAmountField.getText()));
            selectedMembership.setDueAmount(Double.parseDouble(dueAmountField.getText()));
            selectedMembership.setStatus(statusComboBox.getValue());

            if (membershipService.updateMembership(selectedMembership)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Membership updated successfully!");
                clearForm();
                loadMemberships();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update membership.");
            }
        }
    }

    @FXML
    public void deleteMembershipEvent() {
        if (selectedMembership != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirm Delete");
            confirmation.setHeaderText("Delete Membership");
            confirmation.setContentText("Are you sure you want to delete this membership?");

            if (confirmation.showAndWait().get() == ButtonType.OK) {
                if (membershipService.deleteMembership(selectedMembership.getId())) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Membership deleted successfully!");
                    clearForm();
                    loadMemberships();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete membership.");
                }
            }
        }
    }

    @FXML
    public void clearFormEvent() {
        clearForm();
    }

    public void clearForm() {
        memberIdField.clear();
        packageComboBox.setValue(null);
        startDateField.setValue(LocalDate.now());
        endDateField.setValue(null);
        paidAmountField.clear();
        dueAmountField.clear();
        statusComboBox.setValue("Active");

        selectedMembership = null;
        membershipsTable.getSelectionModel().clearSelection();

        // Enable add button, disable update and delete
        addButton.setDisable(false);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    public boolean validateForm() {
        if (memberIdField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Member ID is required.");
            return false;
        }
        if (packageComboBox.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Package is required.");
            return false;
        }
        if (startDateField.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Start date is required.");
            return false;
        }
        if (paidAmountField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Paid amount is required.");
            return false;
        }
        try {
            Double.parseDouble(paidAmountField.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Paid amount must be a valid number.");
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