package bd.edu.seu.gms.gms.Services;

import bd.edu.seu.gms.gms.Interfaces.MemberInterface;
import bd.edu.seu.gms.gms.Models.Member;
import bd.edu.seu.gms.gms.Utilities.ConnectionSingleton;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberService implements MemberInterface {

    @Override
    public boolean addMember(Member member) {
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "INSERT INTO members (member_id, name, email, phone, address, date_of_birth, gender, join_date, status, emergency_contact, medical_conditions) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, member.getMemberId());
            statement.setString(2, member.getName());
            statement.setString(3, member.getEmail());
            statement.setString(4, member.getPhone());
            statement.setString(5, member.getAddress());
            statement.setDate(6, Date.valueOf(member.getDateOfBirth()));
            statement.setString(7, member.getGender());
            statement.setDate(8, Date.valueOf(member.getJoinDate()));
            statement.setString(9, member.getStatus());
            statement.setString(10, member.getEmergencyContact());
            statement.setString(11, member.getMedicalConditions());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateMember(Member member) {
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "UPDATE members SET name = ?, email = ?, phone = ?, address = ?, date_of_birth = ?, gender = ?, join_date = ?, status = ?, emergency_contact = ?, medical_conditions = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, member.getName());
            statement.setString(2, member.getEmail());
            statement.setString(3, member.getPhone());
            statement.setString(4, member.getAddress());
            statement.setDate(5, Date.valueOf(member.getDateOfBirth()));
            statement.setString(6, member.getGender());
            statement.setDate(7, Date.valueOf(member.getJoinDate()));
            statement.setString(8, member.getStatus());
            statement.setString(9, member.getEmergencyContact());
            statement.setString(10, member.getMedicalConditions());
            statement.setInt(11, member.getId());

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteMember(int memberId) {
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "DELETE FROM members WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, memberId);

            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT * FROM members ORDER BY created_date DESC";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Member member = new Member(
                        resultSet.getInt("id"),
                        resultSet.getString("member_id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("address"),
                        resultSet.getDate("date_of_birth").toLocalDate(),
                        resultSet.getString("gender"),
                        resultSet.getDate("join_date").toLocalDate(),
                        resultSet.getString("status"),
                        resultSet.getString("emergency_contact"),
                        resultSet.getString("medical_conditions"),
                        resultSet.getString("created_date")
                );
                members.add(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    @Override
    public Member getMemberById(int memberId) {
        Member member = null;
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT * FROM members WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, memberId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                member = new Member(
                        resultSet.getInt("id"),
                        resultSet.getString("member_id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("address"),
                        resultSet.getDate("date_of_birth").toLocalDate(),
                        resultSet.getString("gender"),
                        resultSet.getDate("join_date").toLocalDate(),
                        resultSet.getString("status"),
                        resultSet.getString("emergency_contact"),
                        resultSet.getString("medical_conditions"),
                        resultSet.getString("created_date")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return member;
    }

    @Override
    public Member getMemberByMemberId(String memberId) {
        Member member = null;
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT * FROM members WHERE member_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, memberId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                member = new Member(
                        resultSet.getInt("id"),
                        resultSet.getString("member_id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("address"),
                        resultSet.getDate("date_of_birth").toLocalDate(),
                        resultSet.getString("gender"),
                        resultSet.getDate("join_date").toLocalDate(),
                        resultSet.getString("status"),
                        resultSet.getString("emergency_contact"),
                        resultSet.getString("medical_conditions"),
                        resultSet.getString("created_date")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return member;
    }

    // Add this method to MemberService class
    public Member getMemberByUserId(int userId) {
        Member member = null;
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT m.* FROM members m JOIN users u ON m.user_id = u.id WHERE u.id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                member = new Member(
                        resultSet.getInt("id"),
                        resultSet.getString("member_id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("address"),
                        resultSet.getDate("date_of_birth").toLocalDate(),
                        resultSet.getString("gender"),
                        resultSet.getDate("join_date").toLocalDate(),
                        resultSet.getString("status"),
                        resultSet.getString("emergency_contact"),
                        resultSet.getString("medical_conditions"),
                        resultSet.getString("created_date")
                );
                member.setUserId(resultSet.getInt("user_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return member;
    }

    @Override
    public List<Member> searchMembers(String keyword) {
        List<Member> members = new ArrayList<>();
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT * FROM members WHERE name LIKE ? OR member_id LIKE ? OR phone LIKE ? OR email LIKE ? ORDER BY created_date DESC";
            PreparedStatement statement = connection.prepareStatement(sql);
            String searchPattern = "%" + keyword + "%";
            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);
            statement.setString(3, searchPattern);
            statement.setString(4, searchPattern);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Member member = new Member(
                        resultSet.getInt("id"),
                        resultSet.getString("member_id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("address"),
                        resultSet.getDate("date_of_birth").toLocalDate(),
                        resultSet.getString("gender"),
                        resultSet.getDate("join_date").toLocalDate(),
                        resultSet.getString("status"),
                        resultSet.getString("emergency_contact"),
                        resultSet.getString("medical_conditions"),
                        resultSet.getString("created_date")
                );
                members.add(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    @Override
    public int getTotalMembers() {
        int count = 0;
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT COUNT(*) as total FROM members";
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
    public int getActiveMembers() {
        int count = 0;
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT COUNT(*) as total FROM members WHERE status = 'active'";
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