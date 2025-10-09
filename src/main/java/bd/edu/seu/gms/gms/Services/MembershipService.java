package bd.edu.seu.gms.gms.Services;

import bd.edu.seu.gms.gms.Models.Package;
import bd.edu.seu.gms.gms.Interfaces.MembershipInterface;
import bd.edu.seu.gms.gms.Models.Membership;
import bd.edu.seu.gms.gms.Utilities.ConnectionSingleton;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MembershipService implements MembershipInterface {

    @Override
    public boolean createMembership(Membership membership) {
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "INSERT INTO memberships (membership_id, member_id, package_id, start_date, end_date, paid_amount, due_amount, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, membership.getMembershipId());
            statement.setString(2, membership.getMemberId());
            statement.setInt(3, membership.getPackageId());
            statement.setDate(4, Date.valueOf(membership.getStartDate()));
            statement.setDate(5, Date.valueOf(membership.getEndDate()));
            statement.setDouble(6, membership.getPaidAmount());
            statement.setDouble(7, membership.getDueAmount());
            statement.setString(8, membership.getStatus());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateMembership(Membership membership) {
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "UPDATE memberships SET member_id = ?, package_id = ?, start_date = ?, end_date = ?, status = ?, paid_amount = ?, due_amount = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, membership.getMemberId());
            statement.setInt(2, membership.getPackageId());
            statement.setDate(3, Date.valueOf(membership.getStartDate()));
            statement.setDate(4, Date.valueOf(membership.getEndDate()));
            statement.setString(5, membership.getStatus());
            statement.setDouble(6, membership.getPaidAmount());
            statement.setDouble(7, membership.getDueAmount());
            statement.setInt(8, membership.getId());

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteMembership(int membershipId) {
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "DELETE FROM memberships WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, membershipId);

            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean renewMembership(int membershipId, int packageId) {
        try {
            Membership membership = getMembershipById(membershipId);
            if (membership == null) return false;

            PackageService packageService = new PackageService();
            Package packageObj = packageService.getPackageById(packageId);
            if (packageObj == null) return false;

            LocalDate newStartDate = LocalDate.now();
            LocalDate newEndDate = newStartDate.plusDays(packageObj.getDurationDays());

            Connection connection = ConnectionSingleton.getConnection();
            String sql = "UPDATE memberships SET package_id = ?, start_date = ?, end_date = ?, status = 'active', due_amount = due_amount + ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, packageId);
            statement.setDate(2, Date.valueOf(newStartDate));
            statement.setDate(3, Date.valueOf(newEndDate));
            statement.setDouble(4, packageObj.getPrice());
            statement.setInt(5, membershipId);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Membership> getAllMemberships() {
        List<Membership> memberships = new ArrayList<>();
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT m.*, mem.name as member_name, mem.phone as member_phone, p.name as package_name " +
                    "FROM memberships m " +
                    "JOIN members mem ON m.member_id = mem.member_id " +
                    "JOIN packages p ON m.package_id = p.id " +
                    "ORDER BY m.start_date DESC";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Membership membership = new Membership(
                        resultSet.getInt("id"),
                        resultSet.getString("membership_id"),
                        resultSet.getString("member_id"),
                        resultSet.getInt("package_id"),
                        resultSet.getDate("start_date").toLocalDate(),
                        resultSet.getDate("end_date").toLocalDate(),
                        resultSet.getDouble("paid_amount"),
                        resultSet.getDouble("due_amount"),
                        resultSet.getString("status")
                );
                membership.setMemberName(resultSet.getString("member_name"));
                membership.setPackageName(resultSet.getString("package_name"));
                membership.setMemberPhone(resultSet.getString("member_phone"));
                memberships.add(membership);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return memberships;
    }

    @Override
    public Membership getMembershipById(int membershipId) {
        Membership membership = null;
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT m.*, mem.name as member_name, mem.phone as member_phone, p.name as package_name " +
                    "FROM memberships m " +
                    "JOIN members mem ON m.member_id = mem.member_id " +
                    "JOIN packages p ON m.package_id = p.id " +
                    "WHERE m.id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, membershipId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                membership = new Membership(
                        resultSet.getInt("id"),
                        resultSet.getString("membership_id"),
                        resultSet.getString("member_id"),
                        resultSet.getInt("package_id"),
                        resultSet.getDate("start_date").toLocalDate(),
                        resultSet.getDate("end_date").toLocalDate(),
                        resultSet.getDouble("paid_amount"),
                        resultSet.getDouble("due_amount"),
                        resultSet.getString("status")
                );
                membership.setMemberName(resultSet.getString("member_name"));
                membership.setPackageName(resultSet.getString("package_name"));
                membership.setMemberPhone(resultSet.getString("member_phone"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return membership;
    }

    @Override
    public List<Membership> getMembershipsByMemberId(int memberId) {
        List<Membership> memberships = new ArrayList<>();
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT m.*, mem.name as member_name, mem.phone as member_phone, p.name as package_name " +
                    "FROM memberships m " +
                    "JOIN members mem ON m.member_id = mem.member_id " +
                    "JOIN packages p ON m.package_id = p.id " +
                    "WHERE mem.id = ? " +
                    "ORDER BY m.start_date DESC";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, memberId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Membership membership = new Membership(
                        resultSet.getInt("id"),
                        resultSet.getString("membership_id"),
                        resultSet.getString("member_id"),
                        resultSet.getInt("package_id"),
                        resultSet.getDate("start_date").toLocalDate(),
                        resultSet.getDate("end_date").toLocalDate(),
                        resultSet.getDouble("paid_amount"),
                        resultSet.getDouble("due_amount"),
                        resultSet.getString("status")
                );
                membership.setMemberName(resultSet.getString("member_name"));
                membership.setPackageName(resultSet.getString("package_name"));
                membership.setMemberPhone(resultSet.getString("member_phone"));
                memberships.add(membership);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return memberships;
    }

    @Override
    public List<Membership> getActiveMemberships() {
        List<Membership> memberships = new ArrayList<>();
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT m.*, mem.name as member_name, mem.phone as member_phone, p.name as package_name " +
                    "FROM memberships m " +
                    "JOIN members mem ON m.member_id = mem.member_id " +
                    "JOIN packages p ON m.package_id = p.id " +
                    "WHERE m.status = 'Active' AND m.end_date >= CURDATE() " +
                    "ORDER BY m.end_date ASC";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Membership membership = new Membership(
                        resultSet.getInt("id"),
                        resultSet.getString("membership_id"),
                        resultSet.getString("member_id"),
                        resultSet.getInt("package_id"),
                        resultSet.getDate("start_date").toLocalDate(),
                        resultSet.getDate("end_date").toLocalDate(),
                        resultSet.getDouble("paid_amount"),
                        resultSet.getDouble("due_amount"),
                        resultSet.getString("status")
                );
                membership.setMemberName(resultSet.getString("member_name"));
                membership.setPackageName(resultSet.getString("package_name"));
                membership.setMemberPhone(resultSet.getString("member_phone"));
                memberships.add(membership);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return memberships;
    }

    @Override
    public List<Membership> getExpiredMemberships() {
        List<Membership> memberships = new ArrayList<>();
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT m.*, mem.name as member_name, mem.phone as member_phone, p.name as package_name " +
                    "FROM memberships m " +
                    "JOIN members mem ON m.member_id = mem.member_id " +
                    "JOIN packages p ON m.package_id = p.id " +
                    "WHERE m.status = 'Active' AND m.end_date < CURDATE() " +
                    "ORDER BY m.end_date DESC";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Membership membership = new Membership(
                        resultSet.getInt("id"),
                        resultSet.getString("membership_id"),
                        resultSet.getString("member_id"),
                        resultSet.getInt("package_id"),
                        resultSet.getDate("start_date").toLocalDate(),
                        resultSet.getDate("end_date").toLocalDate(),
                        resultSet.getDouble("paid_amount"),
                        resultSet.getDouble("due_amount"),
                        resultSet.getString("status")
                );
                membership.setMemberName(resultSet.getString("member_name"));
                membership.setPackageName(resultSet.getString("package_name"));
                membership.setMemberPhone(resultSet.getString("member_phone"));
                memberships.add(membership);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return memberships;
    }

    @Override
    public List<Membership> getMembershipsDueForRenewal(int daysBeforeExpiry) {
        List<Membership> memberships = new ArrayList<>();
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT m.*, mem.name as member_name, mem.phone as member_phone, p.name as package_name " +
                    "FROM memberships m " +
                    "JOIN members mem ON m.member_id = mem.member_id " +
                    "JOIN packages p ON m.package_id = p.id " +
                    "WHERE m.status = 'Active' AND m.end_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL ? DAY) " +
                    "ORDER BY m.end_date ASC";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, daysBeforeExpiry);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Membership membership = new Membership(
                        resultSet.getInt("id"),
                        resultSet.getString("membership_id"),
                        resultSet.getString("member_id"),
                        resultSet.getInt("package_id"),
                        resultSet.getDate("start_date").toLocalDate(),
                        resultSet.getDate("end_date").toLocalDate(),
                        resultSet.getDouble("paid_amount"),
                        resultSet.getDouble("due_amount"),
                        resultSet.getString("status")
                );
                membership.setMemberName(resultSet.getString("member_name"));
                membership.setPackageName(resultSet.getString("package_name"));
                membership.setMemberPhone(resultSet.getString("member_phone"));
                memberships.add(membership);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return memberships;
    }

    @Override
    public double getTotalRevenue() {
        double revenue = 0;
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT SUM(paid_amount) as total FROM memberships";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                revenue = resultSet.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return revenue;
    }

    @Override
    public double getPendingDues() {
        double dues = 0;
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT SUM(due_amount) as total_dues FROM memberships WHERE status = 'Active'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                dues = resultSet.getDouble("total_dues");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dues;
    }

    // Additional method for compatibility with controller
    public boolean insert(Membership membership) {
        return createMembership(membership);
    }

    public boolean update(Membership membership) {
        return updateMembership(membership);
    }

    public boolean delete(int membershipId) {
        return deleteMembership(membershipId);
    }

    public List<Membership> findAll() {
        return getAllMemberships();
    }
}
