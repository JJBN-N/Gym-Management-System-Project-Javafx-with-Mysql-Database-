package bd.edu.seu.gms.gms.Services;

import bd.edu.seu.gms.gms.Interfaces.PackageInterface;
import bd.edu.seu.gms.gms.Models.Package;
import bd.edu.seu.gms.gms.Utilities.ConnectionSingleton;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PackageService implements PackageInterface {

    private static final Logger logger = Logger.getLogger(PackageService.class.getName());

    @Override
    public boolean addPackage(Package packageObj) {
        return insert(packageObj);
    }

    @Override
    public boolean updatePackage(Package packageObj) {
        return update(packageObj);
    }

    @Override
    public boolean deletePackage(int packageId) {
        return delete(packageId);
    }

    @Override
    public List<Package> getAllPackages() {
        return findAll();
    }

    @Override
    public Package getPackageById(int packageId) {
        return findById(packageId);
    }

    @Override
    public List<Package> getPackagesByType(String type) {
        return findByType(type);
    }

    @Override
    public List<Package> getActivePackages() {
        return findActivePackages();
    }

    public boolean insert(Package packageObj) {
        String sql = "INSERT INTO packages (name, type, duration_days, price, description, features, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionSingleton.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, packageObj.getName());
            pstmt.setString(2, packageObj.getType());
            pstmt.setInt(3, packageObj.getDurationDays());
            pstmt.setDouble(4, packageObj.getPrice());
            pstmt.setString(5, packageObj.getDescription());
            pstmt.setString(6, packageObj.getFeatures());
            pstmt.setString(7, packageObj.getStatus());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error inserting package", e);
            return false;
        }
    }

    public boolean update(Package packageObj) {
        String sql = "UPDATE packages SET name = ?, type = ?, duration_days = ?, price = ?, description = ?, features = ?, status = ? WHERE id = ?";

        try (Connection conn = ConnectionSingleton.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, packageObj.getName());
            pstmt.setString(2, packageObj.getType());
            pstmt.setInt(3, packageObj.getDurationDays());
            pstmt.setDouble(4, packageObj.getPrice());
            pstmt.setString(5, packageObj.getDescription());
            pstmt.setString(6, packageObj.getFeatures());
            pstmt.setString(7, packageObj.getStatus());
            pstmt.setInt(8, packageObj.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating package", e);
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM packages WHERE id = ?";

        try (Connection conn = ConnectionSingleton.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting package", e);
            return false;
        }
    }

    public Package findById(int id) {
        String sql = "SELECT * FROM packages WHERE id = ?";
        Package packageObj = null;

        try (Connection conn = ConnectionSingleton.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                packageObj = new Package(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getInt("duration_days"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getString("features"),
                        rs.getString("status")
                );
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding package by ID: " + id, e);
        }
        return packageObj;
    }

    public List<Package> findAll() {
        List<Package> packages = new ArrayList<>();
        String sql = "SELECT * FROM packages ORDER BY id DESC";

        try (Connection conn = ConnectionSingleton.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Package packageObj = new Package(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getInt("duration_days"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getString("features"),
                        rs.getString("status")
                );
                packages.add(packageObj);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding all packages", e);
        }
        return packages;
    }

    public List<Package> findActivePackages() {
        List<Package> packages = new ArrayList<>();
        String sql = "SELECT * FROM packages WHERE status = 'active' ORDER BY name";

        try (Connection conn = ConnectionSingleton.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Package packageObj = new Package(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getInt("duration_days"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getString("features"),
                        rs.getString("status")
                );
                packages.add(packageObj);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding active packages", e);
        }
        return packages;
    }

    public List<Package> findByType(String type) {
        List<Package> packages = new ArrayList<>();
        String sql = "SELECT * FROM packages WHERE type = ? AND status = 'active' ORDER BY name";

        try (Connection conn = ConnectionSingleton.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, type);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Package packageObj = new Package(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getInt("duration_days"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getString("features"),
                        rs.getString("status")
                );
                packages.add(packageObj);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding packages by type: " + type, e);
        }
        return packages;
    }

    public boolean packageNameExists(String name) {
        String sql = "SELECT COUNT(*) FROM packages WHERE name = ?";

        try (Connection conn = ConnectionSingleton.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error checking if package name exists: " + name, e);
        }
        return false;
    }
}