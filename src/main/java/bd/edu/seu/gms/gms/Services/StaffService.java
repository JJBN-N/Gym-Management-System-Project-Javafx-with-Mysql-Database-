package bd.edu.seu.gms.gms.Services;

import bd.edu.seu.gms.gms.Interfaces.StaffInterface;
import bd.edu.seu.gms.gms.Models.Staff;
import bd.edu.seu.gms.gms.Utilities.ConnectionSingleton;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffService implements StaffInterface {

    @Override
    public boolean addStaff(Staff staff) {
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "INSERT INTO staff (staff_id, name, email, phone, address, date_of_birth, gender, hire_date, position, specialization, salary, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, staff.getStaffId());
            statement.setString(2, staff.getName());
            statement.setString(3, staff.getEmail());
            statement.setString(4, staff.getPhone());
            statement.setString(5, staff.getAddress());
            statement.setDate(6, Date.valueOf(staff.getDateOfBirth()));
            statement.setString(7, staff.getGender());
            statement.setDate(8, Date.valueOf(staff.getHireDate()));
            statement.setString(9, staff.getPosition());
            statement.setString(10, staff.getSpecialization());
            statement.setDouble(11, staff.getSalary());
            statement.setString(12, staff.getStatus());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateStaff(Staff staff) {
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "UPDATE staff SET name = ?, email = ?, phone = ?, address = ?, date_of_birth = ?, gender = ?, hire_date = ?, position = ?, specialization = ?, salary = ?, status = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, staff.getName());
            statement.setString(2, staff.getEmail());
            statement.setString(3, staff.getPhone());
            statement.setString(4, staff.getAddress());
            statement.setDate(5, Date.valueOf(staff.getDateOfBirth()));
            statement.setString(6, staff.getGender());
            statement.setDate(7, Date.valueOf(staff.getHireDate()));
            statement.setString(8, staff.getPosition());
            statement.setString(9, staff.getSpecialization());
            statement.setDouble(10, staff.getSalary());
            statement.setString(11, staff.getStatus());
            statement.setInt(12, staff.getId());

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteStaff(int staffId) {
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "DELETE FROM staff WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, staffId);

            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Staff> getAllStaff() {
        List<Staff> staffList = new ArrayList<>();
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT * FROM staff ORDER BY created_date DESC";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Staff staff = new Staff(
                        resultSet.getInt("id"),
                        resultSet.getString("staff_id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("address"),
                        resultSet.getDate("date_of_birth").toLocalDate(),
                        resultSet.getString("gender"),
                        resultSet.getDate("hire_date").toLocalDate(),
                        resultSet.getString("position"),
                        resultSet.getString("specialization"),
                        resultSet.getDouble("salary"),
                        resultSet.getString("status"),
                        resultSet.getString("created_date")
                );
                staffList.add(staff);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffList;
    }

    @Override
    public Staff getStaffById(int staffId) {
        Staff staff = null;
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT * FROM staff WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, staffId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Staff staff1 = new Staff(
                        resultSet.getInt("id"),
                        resultSet.getString("staff_id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("address"),
                        resultSet.getDate("date_of_birth").toLocalDate(),
                        resultSet.getString("gender"),
                        resultSet.getDate("hire_date").toLocalDate(),
                        resultSet.getString("position"),
                        resultSet.getString("specialization"),
                        resultSet.getDouble("salary"),
                        resultSet.getString("status"),
                        resultSet.getString("created_date")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staff;
    }

    @Override
    public Staff getStaffByStaffId(String staffId) {
        Staff staff = null;
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT * FROM staff WHERE staff_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, staffId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                staff = new Staff(
                        resultSet.getInt("id"),
                        resultSet.getString("staff_id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("address"),
                        resultSet.getDate("date_of_birth").toLocalDate(),
                        resultSet.getString("gender"),
                        resultSet.getDate("hire_date").toLocalDate(),
                        resultSet.getString("position"),
                        resultSet.getString("specialization"),
                        resultSet.getDouble("salary"),
                        resultSet.getString("status"),
                        resultSet.getString("created_date")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staff;
    }

    @Override
    public List<Staff> searchStaff(String keyword) {
        List<Staff> staffList = new ArrayList<>();
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT * FROM staff WHERE name LIKE ? OR staff_id LIKE ? OR phone LIKE ? OR email LIKE ? OR position LIKE ? ORDER BY created_date DESC";
            PreparedStatement statement = connection.prepareStatement(sql);
            String searchPattern = "%" + keyword + "%";
            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);
            statement.setString(3, searchPattern);
            statement.setString(4, searchPattern);
            statement.setString(5, searchPattern);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Staff staff = new Staff(
                        resultSet.getInt("id"),
                        resultSet.getString("staff_id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("address"),
                        resultSet.getDate("date_of_birth").toLocalDate(),
                        resultSet.getString("gender"),
                        resultSet.getDate("hire_date").toLocalDate(),
                        resultSet.getString("position"),
                        resultSet.getString("specialization"),
                        resultSet.getDouble("salary"),
                        resultSet.getString("status"),
                        resultSet.getString("created_date")
                );
                staffList.add(staff);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffList;
    }

    @Override
    public int getTotalStaff() {
        int count = 0;
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT COUNT(*) as total FROM staff";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                count = resultSet.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
}