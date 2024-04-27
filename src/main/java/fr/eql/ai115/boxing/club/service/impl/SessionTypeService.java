package fr.eql.ai115.boxing.club.service.impl;

import fr.eql.ai115.boxing.club.entity.SessionType;
import fr.eql.ai115.boxing.club.repository.SessionTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SessionTypeService {

    @Autowired
    SessionTypeDao sessionTypeDao;

    public void saveSessionType(SessionType sessionType) {
        sessionTypeDao.save(sessionType);
    }

    public void deleteSessionType(Long id) {
        sessionTypeDao.deleteById(id);
    }

    public List<SessionType> findAllSessionTypes() {
        return sessionTypeDao.findAll();
    }

    public boolean existsByName(String nameSessionType) {
        return sessionTypeDao.existsByName(nameSessionType);

    }

    public List<SessionType> findByName(String nameSessionType) {
        return sessionTypeDao.findByName(nameSessionType);

    }

    public Optional<SessionType> findSessionTypeById(Long id) {
        return sessionTypeDao.findSessionTypeById(id);
    }
}
