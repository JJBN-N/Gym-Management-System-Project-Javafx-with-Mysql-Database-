package bd.edu.seu.gms.gms.Controllers;

import bd.edu.seu.gms.gms.HelloApplication;
import bd.edu.seu.gms.gms.Services.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class StaffDashboardController implements Initializable {

    @FXML public Label totalMembersLabel;
    @FXML public Label activeMembersLabel;
    @FXML public Label todayCheckinsLabel;
    @FXML public Label welcomeLabel;

    public MemberService memberService;
    public AttendanceService attendanceService;

    public StaffDashboardController() {
        memberService = new MemberService();
        attendanceService = new AttendanceService();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        welcomeLabel.setText("Welcome,  " + LogInController.loggedInUser.getName() + "!");
        updateDashboardStats();
    }

    public void updateDashboardStats() {
        totalMembersLabel.setText(String.valueOf(memberService.getTotalMembers()));
        activeMembersLabel.setText(String.valueOf(memberService.getActiveMembers()));
        todayCheckinsLabel.setText(String.valueOf(attendanceService.getTotalCheckInsToday()));
    }

    @FXML
    public void membersEvent() {
        HelloApplication.changeScene("members");
    }

    @FXML
    public void attendanceEvent() {
        HelloApplication.changeScene("attendance");
    }

    @FXML
    public void membershipsEvent() {
        HelloApplication.changeScene("memberships");
    }

    @FXML
    public void paymentsEvent() {
        HelloApplication.changeScene("payments");
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
