package bd.edu.seu.gms.gms.Controllers;

import bd.edu.seu.gms.gms.HelloApplication;
import bd.edu.seu.gms.gms.Services.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class TrainerDashboardController implements Initializable {

    @FXML public Label welcomeLabel;
    @FXML public Label totalMembersLabel;
    @FXML public Label todayCheckinsLabel;

    public MemberService memberService;
    public AttendanceService attendanceService;

    public TrainerDashboardController() {
        memberService = new MemberService();
        attendanceService = new AttendanceService();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        welcomeLabel.setText("Welcome, " + LogInController.loggedInUser.getName() + "!");
        updateDashboardStats();
    }

    public void updateDashboardStats() {
        totalMembersLabel.setText(String.valueOf(memberService.getTotalMembers()));
        todayCheckinsLabel.setText(String.valueOf(attendanceService.getTotalCheckInsToday()));
    }

    @FXML
    public void memberBmiEvent() {
        HelloApplication.changeScene("trainer-bmi-view");
    }

    @FXML
    public void membersEvent() {
        HelloApplication.changeScene("trainer-members");
    }

    @FXML
    public void attendanceEvent() {
        HelloApplication.changeScene("attendance");
    }

    @FXML
    public void logoutEvent() {
        LogInController.loggedInUser = null;
        HelloApplication.changeScene("login");
    }

    @FXML
    public void refreshEvent() {
        updateDashboardStats();
    }
}
