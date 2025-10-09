package bd.edu.seu.gms.gms.Models;

import java.time.LocalDate;

public class Payment {
    private int id;
    private int membershipId;
    private double amount;
    private LocalDate paymentDate;
    private String paymentMethod;
    private String transactionId;
    private String status;
    private String notes;
    private String createdDate;

    // Additional fields for display
    private String memberName;
    private String packageName;
    private String memberIdStr;

    public Payment() {}

    public Payment(int id, int membershipId, double amount, LocalDate paymentDate,
                   String paymentMethod, String transactionId, String status, String notes, String createdDate) {
        this.id = id;
        this.membershipId = membershipId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.status = status;
        this.notes = notes;
        this.createdDate = createdDate;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMembershipId() { return membershipId; }
    public void setMembershipId(int membershipId) { this.membershipId = membershipId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }

    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }

    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }

    public String getMemberIdStr() { return memberIdStr; }
    public void setMemberIdStr(String memberIdStr) { this.memberIdStr = memberIdStr; }
}
