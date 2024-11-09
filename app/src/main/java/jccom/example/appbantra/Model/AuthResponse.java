package jccom.example.appbantra.Model;

public class AuthResponse {
    private String token;
    private User user;  // Lớp `User` có thể chứa các thông tin người dùng như tên, email, role

    // Constructor
    public AuthResponse(String token, User user) {
        this.token = token;
        this.user = user;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
