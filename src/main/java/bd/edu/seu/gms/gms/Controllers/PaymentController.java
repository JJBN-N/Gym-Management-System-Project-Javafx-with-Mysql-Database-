package bd.edu.seu.gms.gms.Controllers;

import bd.edu.seu.gms.gms.HelloApplication;
import bd.edu.seu.gms.gms.Models.Membership;
import bd.edu.seu.gms.gms.Models.Payment;
import bd.edu.seu.gms.gms.Services.MembershipService;
import bd.edu.seu.gms.gms.Services.PaymentService;
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

public class PaymentController implements Initializable {

    @FXML public TableView<Payment> paymentsTable;
    @FXML public TableColumn<Payment, String> memberNameColumn;
    @FXML public TableColumn<Payment, String> packageNameColumn;
    @FXML public TableColumn<Payment, Double> amountColumn;
    @FXML public TableColumn<Payment, String> paymentDateColumn;
    @FXML public TableColumn<Payment, String> paymentMethodColumn;
    @FXML public TableColumn<Payment, String> statusColumn;

    @FXML public TableView<Membership> membershipsTable;
    @FXML public TableColumn<Membership, String> membershipMemberColumn;
    @FXML public TableColumn<Membership, String> membershipPackageColumn;
    @FXML public TableColumn<Membership, Double> totalAmountColumn;
    @FXML public TableColumn<Membership, Double> paidAmountColumn;
    @FXML public TableColumn<Membership, Double> dueAmountColumn;

    @FXML public ComboBox<Membership> membershipComboBox;
    @FXML public TextField amountField;
    @FXML public DatePicker paymentDateField;
    @FXML public ComboBox<String> paymentMethodField;
    @FXML public TextField transactionIdField;
    @FXML public TextArea notesField;
    @FXML public Label dueAmountLabel;

    @FXML public DatePicker startDateFilter;
    @FXML public DatePicker endDateFilter;

    public PaymentService paymentService;
    public MembershipService membershipService;
    public ObservableList<Payment> paymentsList;
    public ObservableList<Membership> membershipsList;
    public ObservableList<Membership> allMembershipsList;

    public PaymentController() {
        paymentService = new PaymentService();
        membershipService = new MembershipService();
        paymentsList = FXCollections.observableArrayList();
        membershipsList = FXCollections.observableArrayList();
        allMembershipsList = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        loadAllPayments();
        loadMemberships();
        setupForm();
    }

    public void setupTableColumns() {
        // Payments table columns
        memberNameColumn.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        packageNameColumn.setCellValueFactory(new PropertyValueFactory<>("packageName"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        paymentDateColumn.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        paymentMethodColumn.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        paymentsTable.setItems(paymentsList);

        // Memberships table columns
        membershipMemberColumn.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        membershipPackageColumn.setCellValueFactory(new PropertyValueFactory<>("packageName"));
        totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        paidAmountColumn.setCellValueFactory(new PropertyValueFactory<>("paidAmount"));
        dueAmountColumn.setCellValueFactory(new PropertyValueFactory<>("dueAmount"));

        membershipsTable.setItems(membershipsList);
    }

    public void setupForm() {
        paymentMethodField.getItems().addAll("Cash", "Card", "Bank Transfer", "Digital Wallet");
        paymentDateField.setValue(LocalDate.now());

        membershipComboBox.setItems(allMembershipsList);

        membershipComboBox.setCellFactory(param -> new ListCell<Membership>() {
            @Override
            public void updateItem(Membership item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getMemberName() + " - " + item.getPackageName() + " (Due: $" + item.getDueAmount() + ")");
                }
            }
        });

        membershipComboBox.setButtonCell(new ListCell<Membership>() {
            @Override
            public void updateItem(Membership item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getMemberName() + " - " + item.getPackageName() + " (Due: $" + item.getDueAmount() + ")");
                }
            }
        });

        membershipComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> updateDueAmount(newValue));

        startDateFilter.setValue(LocalDate.now().minusDays(30));
        endDateFilter.setValue(LocalDate.now());
    }

    public void loadAllPayments() {
        paymentsList.clear();
        List<Payment> payments = paymentService.getAllPayments();
        paymentsList.addAll(payments);
    }

    public void loadPaymentsByDateRange(LocalDate startDate, LocalDate endDate) {
        paymentsList.clear();
        List<Payment> payments = paymentService.getPaymentsByDateRange(startDate, endDate);
        paymentsList.addAll(payments);
    }

    public void loadMemberships() {
        membershipsList.clear();
        allMembershipsList.clear();

        List<Membership> memberships = membershipService.getActiveMemberships();
        membershipsList.addAll(memberships);

        List<Membership> allMemberships = membershipService.getAllMemberships();
        allMembershipsList.addAll(allMemberships);
    }

    public void updateDueAmount(Membership selectedMembership) {
        if (selectedMembership != null) {
            dueAmountLabel.setText(String.format("$%.2f", selectedMembership.getDueAmount()));
            amountField.setText(String.format("%.2f", selectedMembership.getDueAmount()));
        } else {
            dueAmountLabel.setText("$0.00");
            amountField.clear();
        }
    }

    @FXML
    public void recordPaymentEvent(ActionEvent event) {
        if (validateForm()) {
            Membership selectedMembership = membershipComboBox.getValue();

            Payment payment = new Payment();
            payment.setMembershipId(selectedMembership.getId());
            payment.setAmount(Double.parseDouble(amountField.getText()));
            payment.setPaymentDate(paymentDateField.getValue());
            payment.setPaymentMethod(paymentMethodField.getValue());
            payment.setTransactionId(transactionIdField.getText());
            payment.setStatus("completed");
            payment.setNotes(notesField.getText());

            if (paymentService.recordPayment(payment)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Payment recorded successfully!");
                clearForm();
                loadAllPayments();
                loadMemberships();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to record payment.");
            }
        }
    }

    @FXML
    public void filterPaymentsEvent(ActionEvent event) {
        LocalDate startDate = startDateFilter.getValue();
        LocalDate endDate = endDateFilter.getValue();

        if (startDate != null && endDate != null) {
            if (startDate.isAfter(endDate)) {
                showAlert(Alert.AlertType.ERROR, "Error", "Start date cannot be after end date.");
                return;
            }
            loadPaymentsByDateRange(startDate, endDate);
        }
    }

    @FXML
    public void showAllPaymentsEvent(ActionEvent event) {
        loadAllPayments();
        startDateFilter.setValue(LocalDate.now().minusDays(30));
        endDateFilter.setValue(LocalDate.now());
    }

    @FXML
    public void clearFormEvent(ActionEvent event) {
        clearForm();
    }

    @FXML
    public void backToDashboardEvent(ActionEvent event) {
        HelloApplication.changeScene("dashboard");
    }

    public void clearForm() {
        membershipComboBox.setValue(null);
        amountField.clear();
        paymentDateField.setValue(LocalDate.now());
        paymentMethodField.setValue(null);
        transactionIdField.clear();
        notesField.clear();
        dueAmountLabel.setText("$0.00");
    }

    public boolean validateForm() {
        if (membershipComboBox.getValue() == null || amountField.getText().isEmpty() ||
                paymentDateField.getValue() == null || paymentMethodField.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
            return false;
        }

        try {
            double amount = Double.parseDouble(amountField.getText());
            if (amount <= 0) {
                showAlert(Alert.AlertType.ERROR, "Error", "Amount must be greater than 0.");
                return false;
            }

            Membership selectedMembership = membershipComboBox.getValue();
            if (amount > selectedMembership.getDueAmount()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Amount cannot exceed due amount.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid amount.");
            return false;
        }

        return true;
    }

    public void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}