package fr.eql.ai115.boxing.club.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @JsonIgnore
    private String password;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final List<Session> sessions = new ArrayList<>();

    @ManyToMany(fetch=FetchType.EAGER)
    private List<Role> roles;

    /// Getters ///
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public List<Role> getRoles() {
        return roles;
    }

    /// Setters ///
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    /// Constructors ///
    public Admin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /// Constructors vide ///
    public Admin() {
    }
}