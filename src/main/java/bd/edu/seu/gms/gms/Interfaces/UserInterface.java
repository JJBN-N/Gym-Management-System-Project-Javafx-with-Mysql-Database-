package bd.edu.seu.gms.gms.Interfaces;

import bd.edu.seu.gms.gms.Models.User;
import java.util.List;

public interface UserInterface {
    User authenticate(String username, String password);
    boolean createUser(User user);
    boolean updateUser(User user);
    boolean deleteUser(int userId);
    List<User> getAllUsers();
    User getUserById(int userId);
    User getUserByUsername(String username);
}