package fr.eql.ai115.boxing.club.entity;


import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int durationInHours;


    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id")
    private SessionType sessionType;

    private LocalDate date;
    private int hour;

    @Column(name = "coach_name")
    private String coachName;

    private int maxPeople;

    private String description;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id")
    private Admin admin;

    /// Constructors ///

    public Session(String name, int durationInHours, String description, SessionType sessionType, LocalDate date, int hour, String coachName, int maxPeople) {
        this.name = name;
        this.durationInHours = durationInHours;
        this.description = description;
        this.sessionType = sessionType;
        this.date = date;
        this.hour = hour;
        this.coachName = coachName;
        this.maxPeople = maxPeople;
    }

    /// Constructors vide ///

    public Session() {
    }

    /// Getters ///

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getDurationInHours() {
        return durationInHours;
    }
    public String getDescription() {
        return description;
    }
    public SessionType getSessionType() {
        return sessionType;
    }
    public LocalDate getDate() {
        return date;
    }
    public int getHour() {
        return hour;
    }
    public String getCoachName() {
        return coachName;
    }
    public int getMaxPeople() {
        return maxPeople;
    }
    public Admin getAdmin() {
        return admin;
    }

    /// Setters ///
    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
}
