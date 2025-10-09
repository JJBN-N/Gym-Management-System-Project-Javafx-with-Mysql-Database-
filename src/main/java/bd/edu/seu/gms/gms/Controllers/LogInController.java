package bd.edu.seu.gms.gms.Controllers;

import bd.edu.seu.gms.gms.HelloApplication;
import bd.edu.seu.gms.gms.Models.User;
import bd.edu.seu.gms.gms.Services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LogInController {

    @FXML
    public TextField usernameField;

    @FXML
    public PasswordField passwordField;

    public UserService userService;

    public static User loggedInUser;

    public LogInController() {
        userService = new UserService();
    }

    @FXML
    public void initialize() {
        // Initialization code if needed
    }
    @FXML
    public void registerEvent (ActionEvent event) {
        HelloApplication.changeScene("registration");
    }

    @FXML
    public void loginEvent (ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter both username and password.");
            return;
        }

        User user = userService.authenticate(username, password);
        if (user != null) {
            loggedInUser = user;
            showAlert(Alert.AlertType.INFORMATION, "Success", "Welcome " + user.getName() + "!");

            // Redirect based on user role
            if (user.isAdmin()) {
                HelloApplication.changeScene("adminDashboard");
            } else if (user.isStaff()) {
                HelloApplication.changeScene("staffDashboard");
            } else if (user.isMember()) {
                HelloApplication.changeScene("memberDashboard");
            } else if (user.isTrainer()) {
                HelloApplication.changeScene("trainerDashboard");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Unknown user role.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid username or password.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}