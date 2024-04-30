package fr.eql.ai115.boxing.club.controller.rest;

import fr.eql.ai115.boxing.club.entity.dto.*;
import fr.eql.ai115.boxing.club.jwt.JWTGenerator;
import fr.eql.ai115.boxing.club.service.impl.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("${front.url}")
@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    ApplicationService applicationService;

    @Autowired
    JWTGenerator jwtGenerator;

    @PostMapping("/register")
    public ResponseEntity<String> registerAdmin(@RequestBody AddMemberDto addMemberDto) {
        try {
            String response = applicationService.registerAdmin(addMemberDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/updateAdmin")
    public ResponseEntity<String> updateAdmin(@RequestBody UpdateAdminDto updateAdminDto, @RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Long adminId = jwtGenerator.getUserIdFromToken(token);

            try {
                applicationService.updateAdmin(updateAdminDto, adminId);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Authorization header not found or does not start with Bearer", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeRequestDto passwordChangeRequest, @RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Long adminId = jwtGenerator.getUserIdFromToken(token);

            try {
                applicationService.changeAdminPassword(adminId, passwordChangeRequest);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Authorization header not found or does not start with Bearer", HttpStatus.BAD_REQUEST);
    }
}
