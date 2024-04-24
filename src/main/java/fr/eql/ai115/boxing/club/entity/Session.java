package fr.eql.ai115.boxing.club.entity;


import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int durationInHours;
    private String description;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private SessionType sessionType;

    private LocalDate date;
    private int hour;
    private String coachName;
    private int maxPeople;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Admin admin;

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

    public Long getId() {
        return id;
    }
}
