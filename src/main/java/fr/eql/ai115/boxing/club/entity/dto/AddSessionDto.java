package fr.eql.ai115.boxing.club.entity.dto;

import fr.eql.ai115.boxing.club.entity.SessionType;

import java.time.LocalDate;

public class AddSessionDto {

    private String name;
    private int durationInHours;
    private String description;
    private SessionType sessionType;
    private LocalDate date;
    private int hour;
    private String coachName;
    private int maxPeople;
    private String token;

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
    public String getToken() {
        return token;
    }
}
