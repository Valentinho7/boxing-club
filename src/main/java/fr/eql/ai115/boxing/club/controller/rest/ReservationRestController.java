package fr.eql.ai115.boxing.club.controller.rest;

import fr.eql.ai115.boxing.club.entity.Reservation;
import fr.eql.ai115.boxing.club.entity.dto.ReservationDto;
import fr.eql.ai115.boxing.club.entity.dto.SessionDto;
import fr.eql.ai115.boxing.club.jwt.JWTGenerator;
import fr.eql.ai115.boxing.club.service.impl.ApplicationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("${front.url}")
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
            String token = authHeader.substring(7);
            Long memberId = jwtGenerator.getUserIdFromToken(token);

            applicationService.registerOrder(memberId, sessionIds);
        }
    }

    @GetMapping("/myReservations")
    @Transactional
    public ResponseEntity<List<ReservationDto>> getMemberReservations(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Long memberId = jwtGenerator.getUserIdFromToken(token);

            List<ReservationDto> reservationDtos = applicationService.findAllReservationsByMembers(memberId);
            return ResponseEntity.ok(reservationDtos);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/{reservationId}/sessions")
    public ResponseEntity<List<SessionDto>> getSessionsByReservation(@PathVariable Long reservationId) {
        List<SessionDto> sessionDtos = applicationService.findSessionsByReservationId(reservationId);
        return ResponseEntity.ok(sessionDtos);
    }

    @GetMapping("/validated")
    public ResponseEntity<List<ReservationDto>> getAllValidatedReservations() {
        List<ReservationDto> reservationDtos = applicationService.findAllValidateReservations();
        return ResponseEntity.ok(reservationDtos);
    }

    @PutMapping("/{id}/validate")
    public ResponseEntity<Void> validateReservation(@PathVariable Long id) {
        applicationService.validateReservation(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
