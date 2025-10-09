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

public class TrainerBMIViewController implements Initializable {

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

    public TrainerBMIViewController() {
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
    public void backToDashboardEvent(ActionEvent event) {
        HelloApplication.changeScene("trainer-dashboard");
    }
}
