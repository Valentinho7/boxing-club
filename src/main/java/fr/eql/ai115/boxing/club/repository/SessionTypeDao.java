package fr.eql.ai115.boxing.club.repository;

import fr.eql.ai115.boxing.club.entity.Session;
import fr.eql.ai115.boxing.club.entity.SessionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionTypeDao extends JpaRepository<SessionType, Long> {

    Optional<Session> findSessionTypeById(Long id);
}
