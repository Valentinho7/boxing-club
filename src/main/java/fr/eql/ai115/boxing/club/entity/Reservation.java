package fr.eql.ai115.boxing.club.entity;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Collection;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate orderedDate;
    private LocalDate validateDate;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Status status;

    @ManyToMany(fetch=FetchType.EAGER)
    private Collection<Session> sessions;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Member member;
}
