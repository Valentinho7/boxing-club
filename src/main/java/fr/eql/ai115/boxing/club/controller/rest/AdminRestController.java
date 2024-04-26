package fr.eql.ai115.boxing.club.controller.rest;

import fr.eql.ai115.boxing.club.entity.dto.AddMemberDto;
import fr.eql.ai115.boxing.club.entity.dto.AuthResponseDto;
import fr.eql.ai115.boxing.club.entity.dto.LoginRequest;
import fr.eql.ai115.boxing.club.service.impl.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    ApplicationService applicationService;

    @PostMapping("register")
    public ResponseEntity<String> registerAdmin(@RequestBody AddMemberDto addMemberDto) {
        try {
            String response = applicationService.registerAdmin(addMemberDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequest loginRequest) {
        AuthResponseDto response = applicationService.login(loginRequest, authenticationManager);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
