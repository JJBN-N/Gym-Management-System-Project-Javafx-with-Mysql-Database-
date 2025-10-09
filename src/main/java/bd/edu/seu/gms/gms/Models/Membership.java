package bd.edu.seu.gms.gms.Models;

import java.time.LocalDate;

public class Membership {
    private int id;
    private String membershipId;
    private String memberId;
    private int packageId;
    private LocalDate startDate;
    private LocalDate endDate;
    private double paidAmount;
    private double dueAmount;
    private String status;
    private String packageName; // For display purposes
    private double totalAmount;
    private String memberName;
    private String memberPhone;

    public Membership() {}

    public Membership(int id, String membershipId, String memberId, int packageId,
                      LocalDate startDate, LocalDate endDate, double paidAmount,
                      double dueAmount, String status) {
        this.id = id;
        this.membershipId = membershipId;
        this.memberId = memberId;
        this.packageId = packageId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.paidAmount = paidAmount;
        this.dueAmount = dueAmount;
        this.status = status;
    }

    // Additional constructor for service compatibility
    public Membership(int id, int memberId, int packageId, LocalDate startDate,
                      LocalDate endDate, String status, double totalAmount,
                      double paidAmount, String createdDate) {
        this.id = id;
        this.memberId = String.valueOf(memberId);
        this.packageId = packageId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmount;
        this.dueAmount = totalAmount - paidAmount;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMembershipId() { return membershipId; }
    public void setMembershipId(String membershipId) { this.membershipId = membershipId; }

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public int getPackageId() { return packageId; }
    public void setPackageId(int packageId) { this.packageId = packageId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public double getPaidAmount() { return paidAmount; }
    public void setPaidAmount(double paidAmount) { this.paidAmount = paidAmount; }

    public double getDueAmount() { return dueAmount; }
    public void setDueAmount(double dueAmount) { this.dueAmount = dueAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }

    public String getMemberPhone() { return memberPhone; }
    public void setMemberPhone(String memberPhone) { this.memberPhone = memberPhone; }
}
