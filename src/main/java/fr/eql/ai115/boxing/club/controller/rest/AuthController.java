package fr.eql.ai115.boxing.club.controller.rest;

import fr.eql.ai115.boxing.club.entity.Member;
import fr.eql.ai115.boxing.club.entity.Role;
import fr.eql.ai115.boxing.club.entity.dto.AuthRequest;
import fr.eql.ai115.boxing.club.entity.dto.AuthResponseDto;
import fr.eql.ai115.boxing.club.entity.dto.LoginRequest;
import fr.eql.ai115.boxing.club.jwt.JWTGenerator;
import fr.eql.ai115.boxing.club.repository.MemberDao;
import fr.eql.ai115.boxing.club.repository.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private MemberDao memberDao;
    private RoleDao roleRepository;
    private PasswordEncoder passwordEncoder;
    private JWTGenerator jwtGenerator;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, MemberDao userRepository, RoleDao roleRepository, PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator){
        this.authenticationManager = authenticationManager;
        this.memberDao = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody AuthRequest authRequest) {
        if (memberDao.existsByEmail(authRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already taken!");
        }

        Member member = new Member();
        member.setFirstname(authRequest.getFirstname());
        member.setLastname(authRequest.getLastname());
        member.setBirthdate(authRequest.getBirthdate());
        member.setEmail(authRequest.getEmail());
        member.setPhoneNumber(authRequest.getPhoneNumber());
        member.setAddress(authRequest.getAddress());
        member.setRegistrationDate(LocalDate.now());
        member.setPassword(passwordEncoder.encode(authRequest.getPassword()));


        Role roles = roleRepository.findByName("MEMBER").get();
        member.setRoles(Collections.singletonList(roles));

        memberDao.save(member);

        return  new ResponseEntity<>("User registered successfully!", HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);
    }
}
