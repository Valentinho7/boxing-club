package fr.eql.ai115.boxing.club.controller.rest;

import fr.eql.ai115.boxing.club.jwt.JWTGenerator;
import fr.eql.ai115.boxing.club.service.impl.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationRestController {

    @Autowired
    ApplicationService applicationService;

    @Autowired
    JWTGenerator jwtGenerator;

    @PostMapping
    public void registerOrder(@RequestBody List<Long> sessionIds, @RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Extrait le token sans le préfixe "Bearer "
            Long memberId = jwtGenerator.getUserIdFromToken(token);

            applicationService.registerOrder(memberId, sessionIds);
        }
    }
}
