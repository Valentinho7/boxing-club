package fr.eql.ai115.boxing.club.repository;

import fr.eql.ai115.boxing.club.entity.Reservation;
import fr.eql.ai115.boxing.club.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionDao extends JpaRepository<Session, Long> {


    List<Session> findSessionById(Long id);

}
