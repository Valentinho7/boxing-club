package fr.eql.ai115.boxing.club.controller.rest;

import fr.eql.ai115.boxing.club.entity.dto.AuthResponseDto;
import fr.eql.ai115.boxing.club.entity.dto.LoginRequest;
import fr.eql.ai115.boxing.club.service.impl.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("${front.url}")
@RestController
@RequestMapping("/api")
public class AuthenticationController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    ApplicationService applicationService;

    @PostMapping("login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequest loginRequest) {
        AuthResponseDto response = applicationService.login(loginRequest, authenticationManager);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
