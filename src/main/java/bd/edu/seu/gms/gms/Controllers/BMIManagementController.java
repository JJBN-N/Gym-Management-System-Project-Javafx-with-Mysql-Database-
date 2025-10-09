package bd.edu.seu.gms.gms.Controllers;

import bd.edu.seu.gms.gms.HelloApplication;
import bd.edu.seu.gms.gms.Models.BMIRecord;
import bd.edu.seu.gms.gms.Services.BMIService;
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

public class BMIManagementController implements Initializable {

    @FXML public TableView<BMIRecord> bmiTable;
    @FXML public TableColumn<BMIRecord, String> memberIdColumn;
    @FXML public TableColumn<BMIRecord, String> memberNameColumn;
    @FXML public TableColumn<BMIRecord, Double> heightColumn;
    @FXML public TableColumn<BMIRecord, Double> weightColumn;
    @FXML public TableColumn<BMIRecord, Double> bmiColumn;
    @FXML public TableColumn<BMIRecord, String> categoryColumn;
    @FXML public TableColumn<BMIRecord, LocalDate> dateColumn;

    @FXML public TextField searchField;

    public BMIService bmiService;
    public ObservableList<BMIRecord> bmiList;

    public BMIManagementController() {
        bmiService = new BMIService();
        bmiList = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        loadAllBMIRecords();
    }

    public void setupTableColumns() {
        memberIdColumn.setCellValueFactory(new PropertyValueFactory<>("memberIdStr"));
        memberNameColumn.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        heightColumn.setCellValueFactory(new PropertyValueFactory<>("height"));
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        bmiColumn.setCellValueFactory(new PropertyValueFactory<>("bmiValue"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("recordDate"));

        bmiTable.setItems(bmiList);
    }

    public void loadAllBMIRecords() {
        bmiList.clear();
        List<BMIRecord> records = bmiService.getAllBMIRecords();
        bmiList.addAll(records);
    }

    @FXML
    public void searchEvent(ActionEvent event) {
        String keyword = searchField.getText();
        if (keyword.isEmpty()) {
            loadAllBMIRecords();
        } else {
            // Filter records based on search keyword
            bmiList.clear();
            List<BMIRecord> allRecords = bmiService.getAllBMIRecords();
            for (BMIRecord record : allRecords) {
                if (record.getMemberName().toLowerCase().contains(keyword.toLowerCase()) ||
                        record.getMemberIdStr().toLowerCase().contains(keyword.toLowerCase())) {
                    bmiList.add(record);
                }
            }
        }
    }

    @FXML
    public void deleteRecordEvent(ActionEvent event) {
        BMIRecord selectedRecord = bmiTable.getSelectionModel().getSelectedItem();
        if (selectedRecord != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Delete BMI Record");
            alert.setContentText("Are you sure you want to delete this BMI record?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                if (bmiService.deleteBMIRecord(selectedRecord.getId())) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "BMI record deleted successfully!");
                    loadAllBMIRecords();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete BMI record.");
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a BMI record to delete.");
        }
    }

    @FXML
    public void backToDashboard(ActionEvent event) {
        HelloApplication.changeScene("admin-dashboard");
    }

    public void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
