package bd.edu.seu.gms.gms.Controllers;

import bd.edu.seu.gms.gms.HelloApplication;
import bd.edu.seu.gms.gms.Services.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {

    @FXML public Label totalMembersLabel;
    @FXML public Label activeMembersLabel;
    @FXML public Label totalStaffLabel;
    @FXML public Label todayCheckinsLabel;
    @FXML public Label totalRevenueLabel;
    @FXML public Label pendingDuesLabel;
    @FXML public Label welcomeLabel;
    @FXML public Label totalTrainersLabel;

    public MemberService memberService;
    public StaffService staffService;
    public AttendanceService attendanceService;
    public MembershipService membershipService;
    public TrainerService trainerService;

    public AdminDashboardController() {
        memberService = new MemberService();
        staffService = new StaffService();
        attendanceService = new AttendanceService();
        membershipService = new MembershipService();
        trainerService = new TrainerService();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        welcomeLabel.setText("Welcome,  " + LogInController.loggedInUser.getName() + "!");
        updateDashboardStats();
    }

    public void updateDashboardStats() {
        totalMembersLabel.setText(String.valueOf(memberService.getTotalMembers()));
        activeMembersLabel.setText(String.valueOf(memberService.getActiveMembers()));
        totalStaffLabel.setText(String.valueOf(staffService.getTotalStaff()));
        todayCheckinsLabel.setText(String.valueOf(attendanceService.getTotalCheckInsToday()));
        totalRevenueLabel.setText(String.format("$%.2f", membershipService.getTotalRevenue()));
        pendingDuesLabel.setText(String.format("$%.2f", membershipService.getPendingDues()));
        totalTrainersLabel.setText(String.valueOf(trainerService.getAllTrainers().size()));
    }

    @FXML
    public void membersEvent() {
        HelloApplication.changeScene("members");
    }

    @FXML
    public void staffEvent() {
        HelloApplication.changeScene("staff");
    }

    @FXML
    public void trainerEvent() {
        HelloApplication.changeScene("trainerManagement");
    }

    @FXML
    public void attendanceEvent() {
        HelloApplication.changeScene("attendance");
    }

    @FXML
    public void packagesEvent() {
        HelloApplication.changeScene("packages");
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
    public void BMIManagementEvent() {
        HelloApplication.changeScene("bmiManagement");
    }

    @FXML
    public void userManagementEvent() {
        HelloApplication.changeScene("userManagement");
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