package bd.edu.seu.gms.gms.Interfaces;

import bd.edu.seu.gms.gms.Models.Attendance;
import java.time.LocalDate;
import java.util.List;

public interface AttendanceInterface {
    boolean checkIn(int memberId);
    boolean checkOut(int memberId);
    List<Attendance> getAllAttendance();
    List<Attendance> getAttendanceByMemberId(int memberId);
    List<Attendance> getAttendanceByDate(LocalDate date);
    List<Attendance> getTodayAttendance();
    int getTotalCheckInsToday();
    Attendance getCurrentCheckIn(int memberId);
}
