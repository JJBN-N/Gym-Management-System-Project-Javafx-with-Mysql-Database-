package bd.edu.seu.gms.gms.Controllers;

import bd.edu.seu.gms.gms.HelloApplication;
import bd.edu.seu.gms.gms.Models.Member;
import bd.edu.seu.gms.gms.Models.User;
import bd.edu.seu.gms.gms.Services.MemberService;
import bd.edu.seu.gms.gms.Services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class RegistrationController {

    @FXML public TextField usernameField;
    @FXML public PasswordField passwordField;
    @FXML public PasswordField confirmPasswordField;
    @FXML public TextField nameField;
    @FXML public TextField emailField;
    @FXML public TextField phoneField;
    @FXML public TextField memberIdField;
    @FXML public TextArea addressField;
    @FXML public DatePicker dobField;
    @FXML public ComboBox<String> genderField;
    @FXML public TextField emergencyContactField;
    @FXML public TextArea medicalConditionsField;

    public UserService userService;
    public MemberService memberService;

    public RegistrationController() {
        userService = new UserService();
        memberService = new MemberService();
    }

    @FXML
    public void initialize() {
        genderField.getItems().addAll("Male", "Female", "Other");
    }

    @FXML
    public void registerEvent(ActionEvent event) {
        if (validateForm()) {
            // Create user account
            User user = new User();
            user.setUsername(usernameField.getText());
            user.setPassword(passwordField.getText());
            user.setRole("member");
            user.setName(nameField.getText());
            user.setEmail(emailField.getText());
            user.setPhone(phoneField.getText());
            user.setStatus("active");

            // Create member profile
            Member member = new Member();
            member.setMemberId(memberIdField.getText());
            member.setName(nameField.getText());
            member.setEmail(emailField.getText());
            member.setPhone(phoneField.getText());
            member.setAddress(addressField.getText());
            member.setDateOfBirth(dobField.getValue());
            member.setGender(genderField.getValue());
            member.setJoinDate(LocalDate.now());
            member.setStatus("active");
            member.setEmergencyContact(emergencyContactField.getText());
            member.setMedicalConditions(medicalConditionsField.getText());

            if (registerUserAndMember(user, member)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Registration successful! You can now login.");
                clearForm();
                HelloApplication.changeScene("login");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Registration failed. Please try again.");
            }
        }
    }

    @FXML
    public void BackToLogin(ActionEvent event) {
        HelloApplication.changeScene("login");
    }

    private boolean registerUserAndMember(User user, Member member) {
        try {
            // First create the user account
            if (userService.createUser(user)) {
                // Get the created user ID
                User createdUser = userService.getUserByUsername(user.getUsername());
                if (createdUser != null) {
                    // Link member to user and create member profile
                    member.setUserId(createdUser.getId());

                    // Update the addMember method to handle user_id
                    return addMemberWithUserId(member);
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean addMemberWithUserId(Member member) {
        try {
            java.sql.Connection connection = bd.edu.seu.gms.gms.Utilities.ConnectionSingleton.getConnection();
            String sql = "INSERT INTO members (user_id, member_id, name, email, phone, address, date_of_birth, gender, join_date, status, emergency_contact, medical_conditions) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            java.sql.PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, member.getUserId());
            statement.setString(2, member.getMemberId());
            statement.setString(3, member.getName());
            statement.setString(4, member.getEmail());
            statement.setString(5, member.getPhone());
            statement.setString(6, member.getAddress());
            statement.setDate(7, java.sql.Date.valueOf(member.getDateOfBirth()));
            statement.setString(8, member.getGender());
            statement.setDate(9, java.sql.Date.valueOf(member.getJoinDate()));
            statement.setString(10, member.getStatus());
            statement.setString(11, member.getEmergencyContact());
            statement.setString(12, member.getMedicalConditions());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean validateForm() {
        // Check required fields
        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty() ||
                confirmPasswordField.getText().isEmpty() || nameField.getText().isEmpty() ||
                memberIdField.getText().isEmpty() || emailField.getText().isEmpty() ||
                phoneField.getText().isEmpty() || dobField.getValue() == null ||
                genderField.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
            return false;
        }

        // Check password match
        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match.");
            return false;
        }

        // Check password strength
        if (passwordField.getText().length() < 6) {
            showAlert(Alert.AlertType.ERROR, "Error", "Password must be at least 6 characters long.");
            return false;
        }

        // Check if username already exists
        if (userService.getUserByUsername(usernameField.getText()) != null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Username already exists. Please choose a different username.");
            return false;
        }

        // Check if member ID already exists
        if (memberService.getMemberByMemberId(memberIdField.getText()) != null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Member ID already exists. Please choose a different member ID.");
            return false;
        }

        return true;
    }

    private void clearForm() {
        usernameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        nameField.clear();
        emailField.clear();
        phoneField.clear();
        memberIdField.clear();
        addressField.clear();
        dobField.setValue(null);
        genderField.setValue(null);
        emergencyContactField.clear();
        medicalConditionsField.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
