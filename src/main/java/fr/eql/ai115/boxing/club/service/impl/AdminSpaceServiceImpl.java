package fr.eql.ai115.boxing.club.service.impl;

import fr.eql.ai115.boxing.club.entity.Session;
import fr.eql.ai115.boxing.club.entity.SessionType;
import fr.eql.ai115.boxing.club.entity.dto.AddSessionDto;
import fr.eql.ai115.boxing.club.entity.dto.DeleteSessionDto;
import fr.eql.ai115.boxing.club.repository.SessionDao;
import fr.eql.ai115.boxing.club.service.AdminSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Configuration
public class AdminSpaceServiceImpl implements AdminSpaceService {

    /** Injecté par le setter. */
    @Autowired
    SessionDao sessionDao;


    @Override
    public void saveSession(AddSessionDto addSessionDto) {
//        SessionType sessionType = sessionDao.findSessionTypeByName(addSessionDto.getNameSessionType());
//
//        Session session = new Session(
//                addSessionDto.getName(),
//                addSessionDto.getDurationInHours(),
//                addSessionDto.getDescription(),
//                addSessionDto.,
//                addSessionDto.getDate(),
//                addSessionDto.getHour(),
//                addSessionDto.getCoachName(),
//                addSessionDto.getMaxPeople());
//        sessionDao.save(session);
//        return session;
    }

    @Override
    public void deleteSession(DeleteSessionDto deleteSessionDto, Long id) {
        List<Session> sessions = sessionDao.findSessionById(id);
        Session sessionToDelete = sessions.stream().filter(session -> session.getId().equals(deleteSessionDto.getId())).collect(Collectors.toList()).get(0);
        sessionDao.deleteById(sessionToDelete.getId());
    }

    @Override
    public List<Session> findAllSessions() {
        return sessionDao.findAll();
    }
}
