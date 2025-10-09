package bd.edu.seu.gms.gms.Interfaces;

import bd.edu.seu.gms.gms.Models.Staff;
import java.util.List;

public interface StaffInterface {
    boolean addStaff(Staff staff);
    boolean updateStaff(Staff staff);
    boolean deleteStaff(int staffId);
    List<Staff> getAllStaff();
    Staff getStaffById(int staffId);
    Staff getStaffByStaffId(String staffId);
    List<Staff> searchStaff(String keyword);
    int getTotalStaff();
}