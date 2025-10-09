package bd.edu.seu.gms.gms.Interfaces;

import bd.edu.seu.gms.gms.Models.Payment;
import java.time.LocalDate;
import java.util.List;

public interface PaymentInterface {
    boolean recordPayment(Payment payment);
    List<Payment> getAllPayments();
    List<Payment> getPaymentsByMembershipId(int membershipId);
    List<Payment> getPaymentsByDateRange(LocalDate startDate, LocalDate endDate);
    double getTotalPaymentsByDate(LocalDate date);
    double getMonthlyRevenue(int year, int month);
}