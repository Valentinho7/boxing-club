package fr.eql.ai115.boxing.club.controller.rest;

import fr.eql.ai115.boxing.club.entity.dto.AddMemberDto;
import fr.eql.ai115.boxing.club.entity.dto.AuthResponseDto;
import fr.eql.ai115.boxing.club.entity.dto.LoginRequest;
import fr.eql.ai115.boxing.club.jwt.JWTGenerator;
import fr.eql.ai115.boxing.club.service.impl.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


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



    @PostMapping("register")
    public ResponseEntity<String> registerUser(@RequestBody AddMemberDto addMemberDto) {
        try {
            String response = applicationService.registerUser(addMemberDto);
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

    @PutMapping("/{id}")
    public ResponseEntity<String> updateMember(@RequestBody AddMemberDto addMemberDto, @PathVariable Long id) {
        applicationService.updateMember(addMemberDto, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
