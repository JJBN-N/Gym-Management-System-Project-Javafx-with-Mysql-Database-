package bd.edu.seu.gms.gms.Controllers;

import bd.edu.seu.gms.gms.HelloApplication;
import bd.edu.seu.gms.gms.Models.Attendance;
import bd.edu.seu.gms.gms.Models.Member;
import bd.edu.seu.gms.gms.Services.AttendanceService;
import bd.edu.seu.gms.gms.Services.MemberService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class AttendanceController implements Initializable {

    @FXML public TableView<Attendance> attendanceTable;
    @FXML public TableColumn<Attendance, String> memberIdColumn;
    @FXML public TableColumn<Attendance, String> memberNameColumn;
    @FXML public TableColumn<Attendance, String> checkInColumn;
    @FXML public TableColumn<Attendance, String> checkOutColumn;
    @FXML public TableColumn<Attendance, String> durationColumn;

    @FXML public TextField memberIdField;
    @FXML public Label memberNameLabel;
    @FXML public Label currentStatusLabel;
    @FXML public DatePicker dateFilterField;
    @FXML public Button checkInButton;
    @FXML public Button checkOutButton;

    public AttendanceService attendanceService;
    public MemberService memberService;
    public ObservableList<Attendance> attendanceList;
    public DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public AttendanceController() {
        attendanceService = new AttendanceService();
        memberService = new MemberService();
        attendanceList = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        loadTodayAttendance();
        setupForm();
    }

    public void setupTableColumns() {
        // Set up cell value factories for String columns
        memberIdColumn.setCellValueFactory(new PropertyValueFactory<>("memberIdStr"));
        memberNameColumn.setCellValueFactory(new PropertyValueFactory<>("memberName"));

        // Use custom cell value factories for date/time columns
        checkInColumn.setCellValueFactory(cellData -> {
            LocalDateTime checkIn = cellData.getValue().getCheckIn();
            return new SimpleStringProperty(checkIn != null ? checkIn.format(formatter) : "");
        });

        checkOutColumn.setCellValueFactory(cellData -> {
            LocalDateTime checkOut = cellData.getValue().getCheckOut();
            return new SimpleStringProperty(checkOut != null ? checkOut.format(formatter) : "Not checked out");
        });

        durationColumn.setCellValueFactory(cellData -> {
            Integer duration = cellData.getValue().getDurationMinutes();
            return new SimpleStringProperty(duration != null && duration > 0 ? duration + " minutes" : "");
        });

        attendanceTable.setItems(attendanceList);
    }

    public void setupForm() {
        dateFilterField.setValue(LocalDate.now());
        updateButtonStates();
    }

    public void loadTodayAttendance() {
        attendanceList.clear();
        List<Attendance> attendance = attendanceService.getTodayAttendance();
        attendanceList.addAll(attendance);
    }

    public void loadAttendanceByDate(LocalDate date) {
        attendanceList.clear();
        List<Attendance> attendance = attendanceService.getAttendanceByDate(date);
        attendanceList.addAll(attendance);
    }

    @FXML
    public void checkInEvent(ActionEvent event) {
        String memberId = memberIdField.getText();
        if (memberId.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter member ID.");
            return;
        }

        Member member = memberService.getMemberByMemberId(memberId);
        if (member == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Member not found.");
            return;
        }

        if (!"active".equals(member.getStatus())) {
            showAlert(Alert.AlertType.ERROR, "Error", "Member is not active.");
            return;
        }

        Attendance currentCheckIn = attendanceService.getCurrentCheckIn(member.getId());
        if (currentCheckIn != null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Member is already checked in.");
            return;
        }

        if (attendanceService.checkIn(member.getId())) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Check-in successful for " + member.getName());
            memberNameLabel.setText(member.getName());
            updateButtonStates();
            loadTodayAttendance();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to check in.");
        }
    }

    @FXML
    public void checkOutEvent(ActionEvent event) {
        String memberId = memberIdField.getText();
        if (memberId.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter member ID.");
            return;
        }

        Member member = memberService.getMemberByMemberId(memberId);
        if (member == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Member not found.");
            return;
        }

        Attendance currentCheckIn = attendanceService.getCurrentCheckIn(member.getId());
        if (currentCheckIn == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Member is not checked in.");
            return;
        }

        if (attendanceService.checkOut(member.getId())) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Check-out successful for " + member.getName());
            memberNameLabel.setText("");
            updateButtonStates();
            loadTodayAttendance();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to check out.");
        }
    }

    @FXML
    public void searchMemberEvent(ActionEvent event) {
        String memberId = memberIdField.getText();
        if (memberId.isEmpty()) {
            memberNameLabel.setText("");
            currentStatusLabel.setText("");
            updateButtonStates();
            return;
        }

        Member member = memberService.getMemberByMemberId(memberId);
        if (member != null) {
            memberNameLabel.setText(member.getName());
            Attendance currentCheckIn = attendanceService.getCurrentCheckIn(member.getId());
            if (currentCheckIn != null) {
                currentStatusLabel.setText("Currently Checked In");
                checkInButton.setDisable(true);
                checkOutButton.setDisable(false);
            } else {
                currentStatusLabel.setText("Not Checked In");
                checkInButton.setDisable(false);
                checkOutButton.setDisable(true);
            }
        } else {
            memberNameLabel.setText("Member not found");
            currentStatusLabel.setText("");
            checkInButton.setDisable(true);
            checkOutButton.setDisable(true);
        }
    }

    @FXML
    public void dateFilterEvent(ActionEvent event) {
        LocalDate selectedDate = dateFilterField.getValue();
        if (selectedDate != null) {
            loadAttendanceByDate(selectedDate);
        }
    }

    @FXML
    public void showTodayEvent(ActionEvent event) {
        dateFilterField.setValue(LocalDate.now());
        loadTodayAttendance();
    }

    @FXML
    public void backToDashboard(ActionEvent event) {
        HelloApplication.changeScene("dashboard");
    }

    public void updateButtonStates() {
        String memberId = memberIdField.getText();
        if (memberId.isEmpty()) {
            checkInButton.setDisable(true);
            checkOutButton.setDisable(true);
            return;
        }

        Member member = memberService.getMemberByMemberId(memberId);
        if (member != null) {
            Attendance currentCheckIn = attendanceService.getCurrentCheckIn(member.getId());
            checkInButton.setDisable(currentCheckIn != null);
            checkOutButton.setDisable(currentCheckIn == null);
        } else {
            checkInButton.setDisable(true);
            checkOutButton.setDisable(true);
        }
    }

    public void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
