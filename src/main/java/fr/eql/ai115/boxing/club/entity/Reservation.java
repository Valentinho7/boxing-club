package fr.eql.ai115.boxing.club.entity;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Entity
public class Reservation {

    /// Attributes ///

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate orderedDate;
    private LocalDate validateDate;

    @Column(name = "is_validate", nullable = false, columnDefinition = "boolean default false")
    private boolean isValidate;

    @ManyToMany(fetch=FetchType.EAGER)
    private List<Session> sessions;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Member member;

    /// Empty Constructor ///

    /// Getters ///

    public Long getId() {
        return id;
    }
    public LocalDate getOrderedDate() {
        return orderedDate;
    }
    public LocalDate getValidateDate() {
        return validateDate;
    }
    public boolean isValidate() {
        return isValidate;
    }
    public List<Session> getSessions() {
        return sessions;
    }
    public Member getMember() {
        return member;
    }

    /// Setters ///

    public void setOrderedDate(LocalDate orderedDate) {
        this.orderedDate = orderedDate;
    }
    public void setValidateDate(LocalDate validateDate) {
        this.validateDate = validateDate;
    }
    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }
    public void setMember(Member member) {
        this.member = member;
    }
}
