package fr.eql.ai115.boxing.club.repository;

import fr.eql.ai115.boxing.club.entity.Reservation;
import fr.eql.ai115.boxing.club.entity.Session;
import fr.eql.ai115.boxing.club.entity.SessionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionDao extends JpaRepository<Session, Long> {

    Optional<Session> findSessionById(Long id);

    List<Session> findSessionsBySessionType(SessionType sessionType);
}
