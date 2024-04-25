package fr.eql.ai115.boxing.club.entity.dto;

import fr.eql.ai115.boxing.club.entity.SessionType;

import java.time.LocalDate;

public class DisplaySessionDto {

    private String name;
    private int durationInHours;
    private String description;
    private String nameSessionType;
    private LocalDate date;
    private int hour;
    private String coachName;
    private int maxPeople;

    public String getName() {
        return name;
    }
    public int getDurationInHours() {
        return durationInHours;
    }
    public String getDescription() {
        return description;
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
    public String getNameSessionType() {
        return nameSessionType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDurationInHours(int durationInHours) {
        this.durationInHours = durationInHours;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNameSessionType(String nameSessionType) {
        this.nameSessionType = nameSessionType;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public void setMaxPeople(int maxPeople) {
        this.maxPeople = maxPeople;
    }
}
