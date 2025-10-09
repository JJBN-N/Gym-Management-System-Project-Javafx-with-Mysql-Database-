package bd.edu.seu.gms.gms.Services;

import bd.edu.seu.gms.gms.Interfaces.PaymentInterface;
import bd.edu.seu.gms.gms.Models.Payment;
import bd.edu.seu.gms.gms.Utilities.ConnectionSingleton;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaymentService implements PaymentInterface {

    @Override
    public boolean recordPayment(Payment payment) {
        try {
            Connection connection = ConnectionSingleton.getConnection();

            // Start transaction
            connection.setAutoCommit(false);

            // Insert payment
            String paymentSql = "INSERT INTO payments (membership_id, amount, payment_date, payment_method, transaction_id, status, notes) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement paymentStatement = connection.prepareStatement(paymentSql);
            paymentStatement.setInt(1, payment.getMembershipId());
            paymentStatement.setDouble(2, payment.getAmount());
            paymentStatement.setDate(3, Date.valueOf(payment.getPaymentDate()));
            paymentStatement.setString(4, payment.getPaymentMethod());
            paymentStatement.setString(5, payment.getTransactionId());
            paymentStatement.setString(6, payment.getStatus());
            paymentStatement.setString(7, payment.getNotes());

            int paymentRows = paymentStatement.executeUpdate();

            if (paymentRows > 0) {
                // Update membership paid amount
                String membershipSql = "UPDATE memberships SET paid_amount = paid_amount + ? WHERE id = ?";
                PreparedStatement membershipStatement = connection.prepareStatement(membershipSql);
                membershipStatement.setDouble(1, payment.getAmount());
                membershipStatement.setInt(2, payment.getMembershipId());

                int membershipRows = membershipStatement.executeUpdate();

                if (membershipRows > 0) {
                    connection.commit();
                    return true;
                }
            }

            connection.rollback();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT p.*, m.name as member_name, mem.member_id as member_id_str, pk.name as package_name " +
                    "FROM payments p " +
                    "JOIN memberships ms ON p.membership_id = ms.id " +
                    "JOIN members mem ON ms.member_id = mem.id " +
                    "JOIN packages pk ON ms.package_id = pk.id " +
                    "JOIN users m ON mem.id = m.id " +
                    "ORDER BY p.payment_date DESC";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Payment payment = new Payment(
                        resultSet.getInt("id"),
                        resultSet.getInt("membership_id"),
                        resultSet.getDouble("amount"),
                        resultSet.getDate("payment_date").toLocalDate(),
                        resultSet.getString("payment_method"),
                        resultSet.getString("transaction_id"),
                        resultSet.getString("status"),
                        resultSet.getString("notes"),
                        resultSet.getString("created_date")
                );
                payment.setMemberName(resultSet.getString("member_name"));
                payment.setPackageName(resultSet.getString("package_name"));
                payment.setMemberIdStr(resultSet.getString("member_id_str"));
                payments.add(payment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    @Override
    public List<Payment> getPaymentsByMembershipId(int membershipId) {
        List<Payment> payments = new ArrayList<>();
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT p.*, m.name as member_name, mem.member_id as member_id_str, pk.name as package_name " +
                    "FROM payments p " +
                    "JOIN memberships ms ON p.membership_id = ms.id " +
                    "JOIN members mem ON ms.member_id = mem.id " +
                    "JOIN packages pk ON ms.package_id = pk.id " +
                    "JOIN users m ON mem.id = m.id " +
                    "WHERE p.membership_id = ? " +
                    "ORDER BY p.payment_date DESC";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, membershipId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Payment payment = new Payment(
                        resultSet.getInt("id"),
                        resultSet.getInt("membership_id"),
                        resultSet.getDouble("amount"),
                        resultSet.getDate("payment_date").toLocalDate(),
                        resultSet.getString("payment_method"),
                        resultSet.getString("transaction_id"),
                        resultSet.getString("status"),
                        resultSet.getString("notes"),
                        resultSet.getString("created_date")
                );
                payment.setMemberName(resultSet.getString("member_name"));
                payment.setPackageName(resultSet.getString("package_name"));
                payment.setMemberIdStr(resultSet.getString("member_id_str"));
                payments.add(payment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    @Override
    public List<Payment> getPaymentsByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Payment> payments = new ArrayList<>();
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT p.*, m.name as member_name, mem.member_id as member_id_str, pk.name as package_name " +
                    "FROM payments p " +
                    "JOIN memberships ms ON p.membership_id = ms.id " +
                    "JOIN members mem ON ms.member_id = mem.id " +
                    "JOIN packages pk ON ms.package_id = pk.id " +
                    "JOIN users m ON mem.id = m.id " +
                    "WHERE p.payment_date BETWEEN ? AND ? " +
                    "ORDER BY p.payment_date DESC";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDate(1, Date.valueOf(startDate));
            statement.setDate(2, Date.valueOf(endDate));

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Payment payment = new Payment(
                        resultSet.getInt("id"),
                        resultSet.getInt("membership_id"),
                        resultSet.getDouble("amount"),
                        resultSet.getDate("payment_date").toLocalDate(),
                        resultSet.getString("payment_method"),
                        resultSet.getString("transaction_id"),
                        resultSet.getString("status"),
                        resultSet.getString("notes"),
                        resultSet.getString("created_date")
                );
                payment.setMemberName(resultSet.getString("member_name"));
                payment.setPackageName(resultSet.getString("package_name"));
                payment.setMemberIdStr(resultSet.getString("member_id_str"));
                payments.add(payment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    @Override
    public double getTotalPaymentsByDate(LocalDate date) {
        double total = 0;
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT SUM(amount) as total FROM payments WHERE payment_date = ? AND status = 'completed'";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDate(1, Date.valueOf(date));

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                total = resultSet.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    @Override
    public double getMonthlyRevenue(int year, int month) {
        double revenue = 0;
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT SUM(amount) as total FROM payments WHERE YEAR(payment_date) = ? AND MONTH(payment_date) = ? AND status = 'completed'";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, year);
            statement.setInt(2, month);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                revenue = resultSet.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return revenue;
    }
}
