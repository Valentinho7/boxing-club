package fr.eql.ai115.boxing.club.controller.rest;

import fr.eql.ai115.boxing.club.entity.Session;
import fr.eql.ai115.boxing.club.entity.dto.AddSessionDto;
import fr.eql.ai115.boxing.club.entity.dto.DeleteSessionDto;
import fr.eql.ai115.boxing.club.service.AdminSpaceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/space")
public class AdminSpaceRestController {

    /** Injecté par le setter. */
    AdminSpaceService adminSpaceService;

    @PostMapping
    public Session saveSession(@RequestBody AddSessionDto addSessionDto) {
        return adminSpaceService.saveSession(addSessionDto);
    }

    @DeleteMapping
    public void deleteSession(@RequestBody DeleteSessionDto deleteSessionDto, Long id) {
        adminSpaceService.deleteSession(deleteSessionDto,id);
    }

    @GetMapping
    public List<Session> findAllSession() {
        return adminSpaceService.findAllSessions();
    }
}
