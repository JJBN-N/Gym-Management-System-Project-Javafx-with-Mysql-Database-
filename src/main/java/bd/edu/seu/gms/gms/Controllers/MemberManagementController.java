package bd.edu.seu.gms.gms.Controllers;

import bd.edu.seu.gms.gms.HelloApplication;
import bd.edu.seu.gms.gms.Models.Member;
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

public class MemberManagementController implements Initializable {

    @FXML public TableView<Member> membersTable;
    @FXML public TableColumn<Member, String> idColumn;
    @FXML public TableColumn<Member, String> nameColumn;
    @FXML public TableColumn<Member, String> phoneColumn;
    @FXML public TableColumn<Member, String> emailColumn;
    @FXML public TableColumn<Member, String> statusColumn;

    @FXML public TextField searchField;
    @FXML public TextField memberIdField;
    @FXML public TextField nameField;
    @FXML public TextField emailField;
    @FXML public TextField phoneField;
    @FXML public TextArea addressField;
    @FXML public DatePicker dobField;
    @FXML public ComboBox<String> genderField;
    @FXML public DatePicker joinDateField;
    @FXML public TextField emergencyContactField;
    @FXML public TextArea medicalConditionsField;
    @FXML public ComboBox<String> statusField;

    @FXML public Button addButton;
    @FXML public Button updateButton;
    @FXML public Button deleteButton;
    @FXML public Button clearButton;

    public MemberService memberService;
    public ObservableList<Member> membersList;
    public Member selectedMember;

    public MemberManagementController() {
        memberService = new MemberService();
        membersList = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        loadMembers();
        setupForm();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        membersTable.setItems(membersList);

        // Add selection listener
        membersTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selectMember(newValue));
    }

    private void setupForm() {
        genderField.getItems().addAll("Male", "Female", "Other");
        statusField.getItems().addAll("active", "inactive", "suspended");

        joinDateField.setValue(LocalDate.now());

        // Disable update and delete buttons initially
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    private void loadMembers() {
        membersList.clear();
        List<Member> members = memberService.getAllMembers();
        membersList.addAll(members);
    }

    private void selectMember(Member member) {
        if (member != null) {
            selectedMember = member;
            memberIdField.setText(member.getMemberId());
            nameField.setText(member.getName());
            emailField.setText(member.getEmail());
            phoneField.setText(member.getPhone());
            addressField.setText(member.getAddress());
            dobField.setValue(member.getDateOfBirth());
            genderField.setValue(member.getGender());
            joinDateField.setValue(member.getJoinDate());
            emergencyContactField.setText(member.getEmergencyContact());
            medicalConditionsField.setText(member.getMedicalConditions());
            statusField.setValue(member.getStatus());

            // Enable update and delete buttons
            updateButton.setDisable(false);
            deleteButton.setDisable(false);
            addButton.setDisable(true);
        }
    }

    @FXML
    private void addMemberEvent(ActionEvent event) {
        if (validateForm()) {
            Member member = new Member();
            member.setMemberId(memberIdField.getText());
            member.setName(nameField.getText());
            member.setEmail(emailField.getText());
            member.setPhone(phoneField.getText());
            member.setAddress(addressField.getText());
            member.setDateOfBirth(dobField.getValue());
            member.setGender(genderField.getValue());
            member.setJoinDate(joinDateField.getValue());
            member.setEmergencyContact(emergencyContactField.getText());
            member.setMedicalConditions(medicalConditionsField.getText());
            member.setStatus(statusField.getValue());

            if (memberService.addMember(member)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Member added successfully!");
                clearForm();
                loadMembers();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add member.");
            }
        }
    }

    @FXML
    private void updateMemberEvent(ActionEvent event) {
        if (selectedMember != null && validateForm()) {
            selectedMember.setName(nameField.getText());
            selectedMember.setEmail(emailField.getText());
            selectedMember.setPhone(phoneField.getText());
            selectedMember.setAddress(addressField.getText());
            selectedMember.setDateOfBirth(dobField.getValue());
            selectedMember.setGender(genderField.getValue());
            selectedMember.setJoinDate(joinDateField.getValue());
            selectedMember.setEmergencyContact(emergencyContactField.getText());
            selectedMember.setMedicalConditions(medicalConditionsField.getText());
            selectedMember.setStatus(statusField.getValue());

            if (memberService.updateMember(selectedMember)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Member updated successfully!");
                clearForm();
                loadMembers();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update member.");
            }
        }
    }

    @FXML
    private void deleteMemberEvent(ActionEvent event) {
        if (selectedMember != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Delete Member");
            alert.setContentText("Are you sure you want to delete " + selectedMember.getName() + "?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                if (memberService.deleteMember(selectedMember.getId())) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Member deleted successfully!");
                    clearForm();
                    loadMembers();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete member.");
                }
            }
        }
    }

    @FXML
    private void clearFormEvent(ActionEvent event) {
        clearForm();
    }

    @FXML
    private void searchEvent(ActionEvent event) {
        String keyword = searchField.getText();
        if (keyword.isEmpty()) {
            loadMembers();
        } else {
            membersList.clear();
            List<Member> members = memberService.searchMembers(keyword);
            membersList.addAll(members);
        }
    }

    @FXML
    private void backToDashboardEvent(ActionEvent event) {
        HelloApplication.changeScene("dashboard");
    }

    private void clearForm() {
        memberIdField.clear();
        nameField.clear();
        emailField.clear();
        phoneField.clear();
        addressField.clear();
        dobField.setValue(null);
        genderField.setValue(null);
        joinDateField.setValue(LocalDate.now());
        emergencyContactField.clear();
        medicalConditionsField.clear();
        statusField.setValue("active");

        membersTable.getSelectionModel().clearSelection();
        selectedMember = null;

        // Enable add button, disable update and delete
        addButton.setDisable(false);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    private boolean validateForm() {
        if (memberIdField.getText().isEmpty() || nameField.getText().isEmpty() ||
                phoneField.getText().isEmpty() || joinDateField.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
            return false;
        }
        return true;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}