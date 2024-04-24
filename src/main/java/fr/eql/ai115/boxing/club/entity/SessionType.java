package fr.eql.ai115.boxing.club.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class SessionType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "sessionType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final List<Session> sessions = new ArrayList<>();

}
