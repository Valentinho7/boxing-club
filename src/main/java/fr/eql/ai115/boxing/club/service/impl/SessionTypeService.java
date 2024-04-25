package fr.eql.ai115.boxing.club.service.impl;

import fr.eql.ai115.boxing.club.entity.SessionType;
import fr.eql.ai115.boxing.club.repository.SessionTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Configuration
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
}
