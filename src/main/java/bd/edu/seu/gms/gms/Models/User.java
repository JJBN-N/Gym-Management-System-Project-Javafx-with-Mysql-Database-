package bd.edu.seu.gms.gms.Models;

public class User {
    private int id;
    private String username;
    private String password;
    private String role;
    private String name;
    private String email;
    private String phone;
    private String status;
    private String createdDate;

    public User() {}

    public User(int id, String username, String password, String role, String name,
                String email, String phone, String status, String createdDate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.createdDate = createdDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }

    // Helper methods for role checking
    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(role);
    }

    public boolean isStaff() {
        return "staff".equalsIgnoreCase(role);
    }

    public boolean isMember() {
        return "member".equalsIgnoreCase(role);
    }

    public boolean isTrainer() {
        return "trainer".equalsIgnoreCase(role);
    }
}