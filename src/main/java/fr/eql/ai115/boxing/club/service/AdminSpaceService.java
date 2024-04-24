package fr.eql.ai115.boxing.club.service;

import fr.eql.ai115.boxing.club.entity.Session;
import fr.eql.ai115.boxing.club.entity.dto.AddSessionDto;
import fr.eql.ai115.boxing.club.entity.dto.DeleteSessionDto;

import java.util.List;

public interface AdminSpaceService {

    Session saveSession(AddSessionDto addSessionDto);
    void deleteSession(DeleteSessionDto deleteSessionDto, Long id);
    List<Session> findAllSessions();
}
