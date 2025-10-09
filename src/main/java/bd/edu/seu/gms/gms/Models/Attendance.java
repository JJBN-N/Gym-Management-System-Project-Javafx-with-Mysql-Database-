package bd.edu.seu.gms.gms.Models;

import java.time.LocalDateTime;

public class Attendance {
    private int id;
    private int memberId;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private int durationMinutes;
    private String createdDate;

    // Additional fields for display
    private String memberName;
    private String memberIdStr;

    public Attendance() {}

    public Attendance(int id, int memberId, LocalDateTime checkIn, LocalDateTime checkOut,
                      int durationMinutes, String createdDate) {
        this.id = id;
        this.memberId = memberId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.durationMinutes = durationMinutes;
        this.createdDate = createdDate;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public LocalDateTime getCheckIn() { return checkIn; }
    public void setCheckIn(LocalDateTime checkIn) { this.checkIn = checkIn; }

    public LocalDateTime getCheckOut() { return checkOut; }
    public void setCheckOut(LocalDateTime checkOut) { this.checkOut = checkOut; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }

    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }

    public String getMemberIdStr() { return memberIdStr; }
    public void setMemberIdStr(String memberIdStr) { this.memberIdStr = memberIdStr; }
}