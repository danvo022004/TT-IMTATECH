package jccom.example.appbantra.Model;

public class User {

    private String phone;
    private String password;
    private String name;
    private String email;
    private String role;
    private String id;

    public User(String phone, String password, String name, String email, String role, String id) {
        this.phone = phone;
        this.password = password;
        this.name = name;
        this.email = email;
        this.role = role;
        this.id = id;
    }
    public User(String phone, String password) {
        this.phone = phone;
        this.password = password;

    }
    public User(String phone, String name, String email) {
        this.phone = phone;
        this.name = name;
        this.email = email;
    }


    public User() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
