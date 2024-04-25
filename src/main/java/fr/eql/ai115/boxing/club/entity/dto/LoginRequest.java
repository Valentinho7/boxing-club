package fr.eql.ai115.boxing.club.entity.dto;

import java.util.Objects;

public class LoginRequest {

    private String email;
    private String password;

    // Getters and setters
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }



}