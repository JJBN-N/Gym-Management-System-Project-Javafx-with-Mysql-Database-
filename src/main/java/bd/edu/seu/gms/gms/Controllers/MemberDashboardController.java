package bd.edu.seu.gms.gms.Controllers;

import bd.edu.seu.gms.gms.HelloApplication;
import bd.edu.seu.gms.gms.Models.Member;
import bd.edu.seu.gms.gms.Services.MemberService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class MemberDashboardController implements Initializable {

    @FXML public Label welcomeLabel;
    @FXML public Label memberIdLabel;
    @FXML public Label joinDateLabel;
    @FXML public Label statusLabel;

    public MemberService memberService;
    public Member currentMember;

    public MemberDashboardController() {
        memberService = new MemberService();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadMemberData();
        welcomeLabel.setText("Welcome, " + LogInController.loggedInUser.getName() + "!");
    }

    public void loadMemberData() {
        // Find member by user ID
        currentMember = memberService.getMemberByUserId(LogInController.loggedInUser.getId());
        if (currentMember != null) {
            memberIdLabel.setText(currentMember.getMemberId());
            joinDateLabel.setText(currentMember.getJoinDate().toString());
            statusLabel.setText(currentMember.getStatus());
        }
    }

    @FXML
    public void bmiCalculationEvent() {
        HelloApplication.changeScene("bmi-calculator");
    }

    @FXML
    public void myProfileEvent() {
        HelloApplication.changeScene("member-profile");
    }

    @FXML
    public void myMembershipEvent() {
        HelloApplication.changeScene("member-membership");
    }

    @FXML
    public void myAttendanceEvent() {
        HelloApplication.changeScene("member-attendance");
    }

    @FXML
    public void logoutEvent() {
        LogInController.loggedInUser = null;
        HelloApplication.changeScene("login");
    }
}
