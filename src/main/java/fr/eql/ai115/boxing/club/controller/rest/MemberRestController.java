package fr.eql.ai115.boxing.club.controller.rest;

import fr.eql.ai115.boxing.club.entity.dto.AddMemberDto;
import fr.eql.ai115.boxing.club.entity.dto.AuthResponseDto;
import fr.eql.ai115.boxing.club.entity.dto.LoginRequest;
import fr.eql.ai115.boxing.club.entity.dto.PasswordChangeRequestDto;
import fr.eql.ai115.boxing.club.jwt.JWTGenerator;
import fr.eql.ai115.boxing.club.service.impl.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("${front.url}")
@RestController
@RequestMapping("/api/member")
public class MemberRestController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    ApplicationService applicationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTGenerator jwtGenerator;



    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody AddMemberDto addMemberDto) {
        try {
            String response = applicationService.registerUser(addMemberDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateMember(@RequestBody AddMemberDto addMemberDto, @RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Long memberId = jwtGenerator.getUserIdFromToken(token);

            applicationService.updateMember(addMemberDto, memberId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>("Authorization header not found or does not start with Bearer", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/validatePaymentAndSubscription")
    public ResponseEntity<String> validatePaymentAndSubscription(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Long memberId = jwtGenerator.getUserIdFromToken(token);

            applicationService.validatePaymentAndSubscription(memberId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>("Authorization header not found or does not start with Bearer", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeRequestDto passwordChangeRequest, @RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Long memberId = jwtGenerator.getUserIdFromToken(token);

            try {
                applicationService.changeMemberPassword(memberId, passwordChangeRequest);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Authorization header not found or does not start with Bearer", HttpStatus.BAD_REQUEST);
    }
}
