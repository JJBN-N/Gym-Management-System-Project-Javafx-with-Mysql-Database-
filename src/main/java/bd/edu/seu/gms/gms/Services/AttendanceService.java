package bd.edu.seu.gms.gms.Services;

import bd.edu.seu.gms.gms.Interfaces.AttendanceInterface;
import bd.edu.seu.gms.gms.Models.Attendance;
import bd.edu.seu.gms.gms.Utilities.ConnectionSingleton;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AttendanceService implements AttendanceInterface {

    @Override
    public boolean checkIn(int memberId) {
        try {
            // Check if member already checked in today
            Attendance currentCheckIn = getCurrentCheckIn(memberId);
            if (currentCheckIn != null) {
                return false; // Already checked in
            }

            Connection connection = ConnectionSingleton.getConnection();
            String sql = "INSERT INTO attendance (member_id, check_in, created_date) VALUES (?, NOW(), CURDATE())";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, memberId);

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean checkOut(int memberId) {
        try {
            Attendance currentCheckIn = getCurrentCheckIn(memberId);
            if (currentCheckIn == null) {
                return false; // No active check-in found
            }

            LocalDateTime checkOutTime = LocalDateTime.now();
            long durationMinutes = java.time.Duration.between(currentCheckIn.getCheckIn(), checkOutTime).toMinutes();

            Connection connection = ConnectionSingleton.getConnection();
            String sql = "UPDATE attendance SET check_out = ?, duration_minutes = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setTimestamp(1, Timestamp.valueOf(checkOutTime));
            statement.setInt(2, (int) durationMinutes);
            statement.setInt(3, currentCheckIn.getId());

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Attendance> getAllAttendance() {
        List<Attendance> attendanceList = new ArrayList<>();
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT a.*, m.name as member_name, m.member_id as member_id_str " +
                    "FROM attendance a " +
                    "JOIN members m ON a.member_id = m.id " +
                    "ORDER BY a.check_in DESC";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Attendance attendance = new Attendance(
                        resultSet.getInt("id"),
                        resultSet.getInt("member_id"),
                        resultSet.getTimestamp("check_in").toLocalDateTime(),
                        resultSet.getTimestamp("check_out") != null ? resultSet.getTimestamp("check_out").toLocalDateTime() : null,
                        resultSet.getInt("duration_minutes"),
                        resultSet.getString("created_date")
                );
                attendance.setMemberName(resultSet.getString("member_name"));
                attendance.setMemberIdStr(resultSet.getString("member_id_str"));
                attendanceList.add(attendance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attendanceList;
    }

    @Override
    public List<Attendance> getAttendanceByMemberId(int memberId) {
        List<Attendance> attendanceList = new ArrayList<>();
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT a.*, m.name as member_name, m.member_id as member_id_str " +
                    "FROM attendance a " +
                    "JOIN members m ON a.member_id = m.id " +
                    "WHERE a.member_id = ? " +
                    "ORDER BY a.check_in DESC";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, memberId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Attendance attendance = new Attendance(
                        resultSet.getInt("id"),
                        resultSet.getInt("member_id"),
                        resultSet.getTimestamp("check_in").toLocalDateTime(),
                        resultSet.getTimestamp("check_out") != null ? resultSet.getTimestamp("check_out").toLocalDateTime() : null,
                        resultSet.getInt("duration_minutes"),
                        resultSet.getString("created_date")
                );
                attendance.setMemberName(resultSet.getString("member_name"));
                attendance.setMemberIdStr(resultSet.getString("member_id_str"));
                attendanceList.add(attendance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attendanceList;
    }

    @Override
    public List<Attendance> getAttendanceByDate(LocalDate date) {
        List<Attendance> attendanceList = new ArrayList<>();
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT a.*, m.name as member_name, m.member_id as member_id_str " +
                    "FROM attendance a " +
                    "JOIN members m ON a.member_id = m.id " +
                    "WHERE a.created_date = ? " +
                    "ORDER BY a.check_in DESC";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDate(1, Date.valueOf(date));

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Attendance attendance = new Attendance(
                        resultSet.getInt("id"),
                        resultSet.getInt("member_id"),
                        resultSet.getTimestamp("check_in").toLocalDateTime(),
                        resultSet.getTimestamp("check_out") != null ? resultSet.getTimestamp("check_out").toLocalDateTime() : null,
                        resultSet.getInt("duration_minutes"),
                        resultSet.getString("created_date")
                );
                attendance.setMemberName(resultSet.getString("member_name"));
                attendance.setMemberIdStr(resultSet.getString("member_id_str"));
                attendanceList.add(attendance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attendanceList;
    }

    @Override
    public List<Attendance> getTodayAttendance() {
        return getAttendanceByDate(LocalDate.now());
    }

    @Override
    public int getTotalCheckInsToday() {
        int count = 0;
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT COUNT(*) as total FROM attendance WHERE created_date = CURDATE()";
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

    @Override
    public Attendance getCurrentCheckIn(int memberId) {
        Attendance attendance = null;
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT a.*, m.name as member_name, m.member_id as member_id_str " +
                    "FROM attendance a " +
                    "JOIN members m ON a.member_id = m.id " +
                    "WHERE a.member_id = ? AND a.created_date = CURDATE() AND a.check_out IS NULL " +
                    "ORDER BY a.check_in DESC LIMIT 1";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, memberId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                attendance = new Attendance(
                        resultSet.getInt("id"),
                        resultSet.getInt("member_id"),
                        resultSet.getTimestamp("check_in").toLocalDateTime(),
                        null,
                        0,
                        resultSet.getString("created_date")
                );
                attendance.setMemberName(resultSet.getString("member_name"));
                attendance.setMemberIdStr(resultSet.getString("member_id_str"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attendance;
    }
}
