package bd.edu.seu.gms.gms.Controllers;

import bd.edu.seu.gms.gms.HelloApplication;
import bd.edu.seu.gms.gms.Models.BMIRecord;
import bd.edu.seu.gms.gms.Models.Member;
import bd.edu.seu.gms.gms.Services.BMIService;
import bd.edu.seu.gms.gms.Services.MemberService;
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

public class BMICalculatorController implements Initializable {

    @FXML public TextField heightField;
    @FXML public TextField weightField;
    @FXML public Label bmiResultLabel;
    @FXML public Label categoryLabel;
    @FXML public TextArea notesField;

    @FXML public TableView<BMIRecord> bmiHistoryTable;
    @FXML public TableColumn<BMIRecord, Double> heightColumn;
    @FXML public TableColumn<BMIRecord, Double> weightColumn;
    @FXML public TableColumn<BMIRecord, Double> bmiColumn;
    @FXML public TableColumn<BMIRecord, String> categoryColumn;
    @FXML public TableColumn<BMIRecord, LocalDate> dateColumn;

    public BMIService bmiService;
    public MemberService memberService;
    public ObservableList<BMIRecord> bmiHistoryList;
    public Member currentMember;

    public BMICalculatorController() {
        bmiService = new BMIService();
        memberService = new MemberService();
        bmiHistoryList = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        loadMemberData();
        loadBMIHistory();
    }

    public void setupTableColumns() {
        heightColumn.setCellValueFactory(new PropertyValueFactory<>("height"));
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        bmiColumn.setCellValueFactory(new PropertyValueFactory<>("bmiValue"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("recordDate"));

        bmiHistoryTable.setItems(bmiHistoryList);
    }

    public void loadMemberData() {
        currentMember = memberService.getMemberByUserId(LogInController.loggedInUser.getId());
    }

    public void loadBMIHistory() {
        if (currentMember != null) {
            bmiHistoryList.clear();
            List<BMIRecord> records = bmiService.getBMIRecordsByMember(currentMember.getId());
            bmiHistoryList.addAll(records);
        }
    }

    @FXML
    public void calculateBmiEvent(ActionEvent event) {
        if (validateInput()) {
            double height = Double.parseDouble(heightField.getText());
            double weight = Double.parseDouble(weightField.getText());

            double bmi = bmiService.calculateBMI(height, weight);
            String category = bmiService.getBMICategory(bmi);

            bmiResultLabel.setText(String.format("%.2f", bmi));
            categoryLabel.setText(category);
        }
    }

    @FXML
    public void saveRecordEvent(ActionEvent event) {
        if (validateInput() && currentMember != null) {
            double height = Double.parseDouble(heightField.getText());
            double weight = Double.parseDouble(weightField.getText());
            double bmi = bmiService.calculateBMI(height, weight);
            String category = bmiService.getBMICategory(bmi);

            BMIRecord record = new BMIRecord();
            record.setMemberId(currentMember.getId());
            record.setHeight(height);
            record.setWeight(weight);
            record.setBmiValue(bmi);
            record.setCategory(category);
            record.setRecordDate(LocalDate.now());
            record.setNotes(notesField.getText());

            if (bmiService.addBMIRecord(record)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "BMI record saved successfully!");
                clearForm();
                loadBMIHistory();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to save BMI record.");
            }
        }
    }

    @FXML
    public void backToDashboard(ActionEvent event) {
        HelloApplication.changeScene("member-dashboard");
    }

    public boolean validateInput() {
        if (heightField.getText().isEmpty() || weightField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter both height and weight.");
            return false;
        }

        try {
            double height = Double.parseDouble(heightField.getText());
            double weight = Double.parseDouble(weightField.getText());

            if (height <= 0 || weight <= 0) {
                showAlert(Alert.AlertType.ERROR, "Error", "Height and weight must be positive values.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter valid numbers for height and weight.");
            return false;
        }

        return true;
    }

    public void clearForm() {
        heightField.clear();
        weightField.clear();
        bmiResultLabel.setText("--");
        categoryLabel.setText("--");
        notesField.clear();
    }

    public void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}