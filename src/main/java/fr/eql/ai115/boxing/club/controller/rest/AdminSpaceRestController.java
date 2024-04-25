package fr.eql.ai115.boxing.club.controller.rest;

import fr.eql.ai115.boxing.club.entity.Session;
import fr.eql.ai115.boxing.club.entity.SessionType;
import fr.eql.ai115.boxing.club.entity.dto.AddSessionDto;
import fr.eql.ai115.boxing.club.entity.dto.DeleteSessionDto;
import fr.eql.ai115.boxing.club.entity.dto.DisplaySessionDto;
import fr.eql.ai115.boxing.club.service.AdminSpaceService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("api/admin/space")
public class AdminSpaceRestController {

    /** Injecté par le setter. */
    @Autowired
    AdminSpaceService adminSpaceService;

    Logger log = Logger.getLogger(AdminSpaceRestController.class.getName());

    @PostMapping
    public void saveSession(@RequestBody AddSessionDto addSessionDto) {
         adminSpaceService.saveSession(addSessionDto);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSession(@RequestBody DeleteSessionDto deleteSessionDto, Long id) {
        adminSpaceService.deleteSession(deleteSessionDto,id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    @Transactional
    public ResponseEntity<List<DisplaySessionDto>> findAllSession() {
        List<Session> sessions = adminSpaceService.findAllSessions();
        List<DisplaySessionDto> response =  mapSessionToDisplaySessionDto(sessions);
        if(!response.isEmpty()) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    private  List<DisplaySessionDto> mapSessionToDisplaySessionDto(List<Session> sessions) {
        List<DisplaySessionDto> displaySessionDtos = new ArrayList<>();
        for (Session session : sessions) {
            DisplaySessionDto displaySessionDto = new DisplaySessionDto();
            displaySessionDto.setName(session.getName());
            displaySessionDto.setDurationInHours(session.getDurationInHours());
            displaySessionDto.setDescription(session.getDescription());
            SessionType sessionType = session.getSessionType();
            displaySessionDto.setNameSessionType(sessionType.getName());
            displaySessionDto.setDate(session.getDate());
            displaySessionDto.setHour(session.getHour());
            displaySessionDto.setCoachName(session.getCoachName());
            displaySessionDto.setMaxPeople(session.getMaxPeople());
            displaySessionDtos.add(displaySessionDto);
        }
        return displaySessionDtos;
    }
}
