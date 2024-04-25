package fr.eql.ai115.boxing.club.service.impl;

import fr.eql.ai115.boxing.club.entity.Session;
import fr.eql.ai115.boxing.club.entity.dto.DeleteSessionDto;
import fr.eql.ai115.boxing.club.repository.SessionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Configuration
public class SessionService {


    @Autowired
    SessionDao sessionDao;


    public void saveSession(Session session) {
        sessionDao.save(session);
    }

    public void deleteSession(Long id) {
        sessionDao.deleteById(id);
    }

    public Optional<Session> findSessionById(Long id) {
        return sessionDao.findSessionById(id);
    }

    public List<Session> findAllSessions() {
        return sessionDao.findAll();
    }
}
