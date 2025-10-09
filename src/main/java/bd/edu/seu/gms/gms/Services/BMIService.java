package bd.edu.seu.gms.gms.Services;

import bd.edu.seu.gms.gms.Interfaces.BMIInterface;
import bd.edu.seu.gms.gms.Models.BMIRecord;
import bd.edu.seu.gms.gms.Utilities.ConnectionSingleton;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BMIService implements BMIInterface {

    @Override
    public boolean addBMIRecord(BMIRecord bmiRecord) {
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "INSERT INTO bmi_records (member_id, height, weight, bmi_value, category, record_date, notes) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, bmiRecord.getMemberId());
            statement.setDouble(2, bmiRecord.getHeight());
            statement.setDouble(3, bmiRecord.getWeight());
            statement.setDouble(4, bmiRecord.getBmiValue());
            statement.setString(5, bmiRecord.getCategory());
            statement.setDate(6, Date.valueOf(bmiRecord.getRecordDate()));
            statement.setString(7, bmiRecord.getNotes());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<BMIRecord> getBMIRecordsByMember(int memberId) {
        List<BMIRecord> records = new ArrayList<>();
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT br.*, m.name as member_name, m.member_id as member_id_str " +
                    "FROM bmi_records br " +
                    "JOIN members m ON br.member_id = m.id " +
                    "WHERE br.member_id = ? " +
                    "ORDER BY br.record_date DESC";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, memberId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                BMIRecord record = new BMIRecord(
                        resultSet.getInt("id"),
                        resultSet.getInt("member_id"),
                        resultSet.getDouble("height"),
                        resultSet.getDouble("weight"),
                        resultSet.getDouble("bmi_value"),
                        resultSet.getString("category"),
                        resultSet.getDate("record_date").toLocalDate(),
                        resultSet.getString("notes")
                );
                record.setMemberName(resultSet.getString("member_name"));
                record.setMemberIdStr(resultSet.getString("member_id_str"));
                records.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    @Override
    public List<BMIRecord> getAllBMIRecords() {
        List<BMIRecord> records = new ArrayList<>();
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT br.*, m.name as member_name, m.member_id as member_id_str " +
                    "FROM bmi_records br " +
                    "JOIN members m ON br.member_id = m.id " +
                    "ORDER BY br.record_date DESC";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                BMIRecord record = new BMIRecord(
                        resultSet.getInt("id"),
                        resultSet.getInt("member_id"),
                        resultSet.getDouble("height"),
                        resultSet.getDouble("weight"),
                        resultSet.getDouble("bmi_value"),
                        resultSet.getString("category"),
                        resultSet.getDate("record_date").toLocalDate(),
                        resultSet.getString("notes")
                );
                record.setMemberName(resultSet.getString("member_name"));
                record.setMemberIdStr(resultSet.getString("member_id_str"));
                records.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    @Override
    public BMIRecord getLatestBMIRecord(int memberId) {
        BMIRecord record = null;
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT br.*, m.name as member_name, m.member_id as member_id_str " +
                    "FROM bmi_records br " +
                    "JOIN members m ON br.member_id = m.id " +
                    "WHERE br.member_id = ? " +
                    "ORDER BY br.record_date DESC LIMIT 1";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, memberId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                record = new BMIRecord(
                        resultSet.getInt("id"),
                        resultSet.getInt("member_id"),
                        resultSet.getDouble("height"),
                        resultSet.getDouble("weight"),
                        resultSet.getDouble("bmi_value"),
                        resultSet.getString("category"),
                        resultSet.getDate("record_date").toLocalDate(),
                        resultSet.getString("notes")
                );
                record.setMemberName(resultSet.getString("member_name"));
                record.setMemberIdStr(resultSet.getString("member_id_str"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return record;
    }

    @Override
    public boolean deleteBMIRecord(int recordId) {
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "DELETE FROM bmi_records WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, recordId);

            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public double calculateBMI(double height, double weight) {
        double heightInMeters = height / 100.0;
        return weight / (heightInMeters * heightInMeters);
    }

    @Override
    public String getBMICategory(double bmi) {
        if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi < 25) {
            return "Normal weight";
        } else if (bmi < 30) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }
}