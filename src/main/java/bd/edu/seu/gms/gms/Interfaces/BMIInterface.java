package bd.edu.seu.gms.gms.Interfaces;

import bd.edu.seu.gms.gms.Models.BMIRecord;
import java.util.List;

public interface BMIInterface {
    boolean addBMIRecord(BMIRecord bmiRecord);
    List<BMIRecord> getBMIRecordsByMember(int memberId);
    List<BMIRecord> getAllBMIRecords();
    BMIRecord getLatestBMIRecord(int memberId);
    boolean deleteBMIRecord(int recordId);
    double calculateBMI(double height, double weight);
    String getBMICategory(double bmi);
}