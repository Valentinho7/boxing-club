package fr.eql.ai115.boxing.club.entity;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class SessionType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "sessionType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final List<Session> sessions = new ArrayList<>();

    /// Getters ///
    public String getName() {
        return name;
    }

    /// Setters ///
    public void setName(String name) {
        this.name = name;
    }

    /// Constructors ///
    public SessionType(String name) {
        this.name = name;
    }

    /// Constructors vide ///
    public SessionType() {
    }
}
