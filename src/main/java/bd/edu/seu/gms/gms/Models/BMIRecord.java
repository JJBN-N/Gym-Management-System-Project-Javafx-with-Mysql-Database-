package bd.edu.seu.gms.gms.Models;

import java.time.LocalDate;

public class BMIRecord {
    private int id;
    private int memberId;
    private double height;
    private double weight;
    private double bmiValue;
    private String category;
    private LocalDate recordDate;
    private String notes;

    // Additional fields for display
    private String memberName;
    private String memberIdStr;

    public BMIRecord() {}

    public BMIRecord(int id, int memberId, double height, double weight,
                     double bmiValue, String category, LocalDate recordDate, String notes) {
        this.id = id;
        this.memberId = memberId;
        this.height = height;
        this.weight = weight;
        this.bmiValue = bmiValue;
        this.category = category;
        this.recordDate = recordDate;
        this.notes = notes;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public double getBmiValue() { return bmiValue; }
    public void setBmiValue(double bmiValue) { this.bmiValue = bmiValue; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public LocalDate getRecordDate() { return recordDate; }
    public void setRecordDate(LocalDate recordDate) { this.recordDate = recordDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }

    public String getMemberIdStr() { return memberIdStr; }
    public void setMemberIdStr(String memberIdStr) { this.memberIdStr = memberIdStr; }
}