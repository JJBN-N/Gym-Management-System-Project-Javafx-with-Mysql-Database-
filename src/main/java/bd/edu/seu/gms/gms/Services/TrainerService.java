package bd.edu.seu.gms.gms.Services;

import bd.edu.seu.gms.gms.Interfaces.TrainerInterface;
import bd.edu.seu.gms.gms.Models.Trainer;
import bd.edu.seu.gms.gms.Models.User;
import bd.edu.seu.gms.gms.Utilities.ConnectionSingleton;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TrainerService implements TrainerInterface {

    @Override
    public boolean addTrainer(Trainer trainer) {
        try {
            Connection connection = ConnectionSingleton.getConnection();

            // First create user
            String userSql = "INSERT INTO users (username, password, role, name, email, phone) VALUES (?, ?, 'trainer', ?, ?, ?)";
            PreparedStatement userStmt = connection.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS);
            userStmt.setString(1, trainer.getUser().getUsername());
            userStmt.setString(2, trainer.getUser().getPassword());
            userStmt.setString(3, trainer.getUser().getName());
            userStmt.setString(4, trainer.getUser().getEmail());
            userStmt.setString(5, trainer.getUser().getPhone());

            userStmt.executeUpdate();

            ResultSet generatedKeys = userStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int userId = generatedKeys.getInt(1);

                // Then create trainer
                String trainerSql = "INSERT INTO trainers (user_id, specialization, experience_years, salary, hire_date, status) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement trainerStmt = connection.prepareStatement(trainerSql);
                trainerStmt.setInt(1, userId);
                trainerStmt.setString(2, trainer.getSpecialization());
                trainerStmt.setInt(3, trainer.getExperienceYears());
                trainerStmt.setDouble(4, trainer.getSalary());
                trainerStmt.setDate(5, Date.valueOf(trainer.getHireDate()));
                trainerStmt.setString(6, trainer.getStatus());

                return trainerStmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Trainer> getAllTrainers() {
        List<Trainer> trainers = new ArrayList<>();
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT t.*, u.name, u.email, u.phone, u.username FROM trainers t JOIN users u ON t.user_id = u.id";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Trainer trainer = new Trainer();
                trainer.setId(resultSet.getInt("id"));
                trainer.setUserId(resultSet.getInt("user_id"));
                trainer.setSpecialization(resultSet.getString("specialization"));
                trainer.setExperienceYears(resultSet.getInt("experience_years"));
                trainer.setSalary(resultSet.getDouble("salary"));
                trainer.setHireDate(resultSet.getDate("hire_date").toLocalDate());
                trainer.setStatus(resultSet.getString("status"));

                User user = new User();
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
                user.setPhone(resultSet.getString("phone"));
                user.setUsername(resultSet.getString("username"));
                trainer.setUser(user);

                trainers.add(trainer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trainers;
    }

    @Override
    public List<Trainer> getActiveTrainers() {
        List<Trainer> trainers = new ArrayList<>();
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT t.*, u.name, u.email, u.phone, u.username FROM trainers t JOIN users u ON t.user_id = u.id WHERE t.status = 'active'";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Trainer trainer = new Trainer();
                trainer.setId(resultSet.getInt("id"));
                trainer.setUserId(resultSet.getInt("user_id"));
                trainer.setSpecialization(resultSet.getString("specialization"));
                trainer.setExperienceYears(resultSet.getInt("experience_years"));
                trainer.setSalary(resultSet.getDouble("salary"));
                trainer.setHireDate(resultSet.getDate("hire_date").toLocalDate());
                trainer.setStatus(resultSet.getString("status"));

                User user = new User();
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
                user.setPhone(resultSet.getString("phone"));
                user.setUsername(resultSet.getString("username"));
                trainer.setUser(user);

                trainers.add(trainer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trainers;
    }

    @Override
    public boolean updateTrainer(Trainer trainer) {
        try {
            Connection connection = ConnectionSingleton.getConnection();

            // Update user information
            String userSql = "UPDATE users SET name = ?, email = ?, phone = ? WHERE id = ?";
            PreparedStatement userStmt = connection.prepareStatement(userSql);
            userStmt.setString(1, trainer.getUser().getName());
            userStmt.setString(2, trainer.getUser().getEmail());
            userStmt.setString(3, trainer.getUser().getPhone());
            userStmt.setInt(4, trainer.getUserId());
            userStmt.executeUpdate();

            // Update trainer information
            String trainerSql = "UPDATE trainers SET specialization = ?, experience_years = ?, salary = ?, status = ? WHERE id = ?";
            PreparedStatement trainerStmt = connection.prepareStatement(trainerSql);
            trainerStmt.setString(1, trainer.getSpecialization());
            trainerStmt.setInt(2, trainer.getExperienceYears());
            trainerStmt.setDouble(3, trainer.getSalary());
            trainerStmt.setString(4, trainer.getStatus());
            trainerStmt.setInt(5, trainer.getId());

            return trainerStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateTrainerStatus(int trainerId, String status) {
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "UPDATE trainers SET status = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, trainerId);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Trainer getTrainerById(int trainerId) {
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT t.*, u.name, u.email, u.phone, u.username FROM trainers t JOIN users u ON t.user_id = u.id WHERE t.id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, trainerId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Trainer trainer = new Trainer();
                trainer.setId(resultSet.getInt("id"));
                trainer.setUserId(resultSet.getInt("user_id"));
                trainer.setSpecialization(resultSet.getString("specialization"));
                trainer.setExperienceYears(resultSet.getInt("experience_years"));
                trainer.setSalary(resultSet.getDouble("salary"));
                trainer.setHireDate(resultSet.getDate("hire_date").toLocalDate());
                trainer.setStatus(resultSet.getString("status"));

                User user = new User();
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
                user.setPhone(resultSet.getString("phone"));
                user.setUsername(resultSet.getString("username"));
                trainer.setUser(user);

                return trainer;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Trainer getTrainerByUserId(int userId) {
        try {
            Connection connection = ConnectionSingleton.getConnection();
            String sql = "SELECT t.*, u.name, u.email, u.phone, u.username FROM trainers t JOIN users u ON t.user_id = u.id WHERE t.user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Trainer trainer = new Trainer();
                trainer.setId(resultSet.getInt("id"));
                trainer.setUserId(resultSet.getInt("user_id"));
                trainer.setSpecialization(resultSet.getString("specialization"));
                trainer.setExperienceYears(resultSet.getInt("experience_years"));
                trainer.setSalary(resultSet.getDouble("salary"));
                trainer.setHireDate(resultSet.getDate("hire_date").toLocalDate());
                trainer.setStatus(resultSet.getString("status"));

                User user = new User();
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
                user.setPhone(resultSet.getString("phone"));
                user.setUsername(resultSet.getString("username"));
                trainer.setUser(user);

                return trainer;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteTrainer(int trainerId) {
        try {
            Connection connection = ConnectionSingleton.getConnection();

            // First get user_id
            String getUserIdSql = "SELECT user_id FROM trainers WHERE id = ?";
            PreparedStatement getUserIdStmt = connection.prepareStatement(getUserIdSql);
            getUserIdStmt.setInt(1, trainerId);
            ResultSet resultSet = getUserIdStmt.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("user_id");

                // Delete trainer record
                String deleteTrainerSql = "DELETE FROM trainers WHERE id = ?";
                PreparedStatement deleteTrainerStmt = connection.prepareStatement(deleteTrainerSql);
                deleteTrainerStmt.setInt(1, trainerId);
                deleteTrainerStmt.executeUpdate();

                // Delete user record
                String deleteUserSql = "DELETE FROM users WHERE id = ?";
                PreparedStatement deleteUserStmt = connection.prepareStatement(deleteUserSql);
                deleteUserStmt.setInt(1, userId);

                return deleteUserStmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
