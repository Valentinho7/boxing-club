package fr.eql.ai115.boxing.club.service.impl;

import fr.eql.ai115.boxing.club.entity.*;
import fr.eql.ai115.boxing.club.entity.dto.*;
import fr.eql.ai115.boxing.club.jwt.JWTGenerator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    @Autowired
    SessionService sessionService;

    @Autowired
    SessionTypeService sessionTypeService;

    @Autowired
    MemberService memberService;

    @Autowired
    AdminService adminService;

    @Autowired
    RoleService roleService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JWTGenerator jwtGenerator;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    /// Session part ///

    public void saveSession(AddSessionDto addSessionDto) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Admin admin = adminService.findByEmail(userDetails.getUsername()).get();

        SessionType sessionType;
        if (sessionTypeService.existsByName(addSessionDto.getNameSessionType())) {

            sessionType = sessionTypeService.findByName(addSessionDto.getNameSessionType()).get(0);
        } else {

            sessionType = new SessionType(addSessionDto.getNameSessionType());
            sessionTypeService.saveSessionType(sessionType);
        }

        Session session = new Session(
                addSessionDto.getName(),
                addSessionDto.getDurationInHours(),
                addSessionDto.getDescription(),
                sessionType,
                addSessionDto.getDate(),
                addSessionDto.getHour(),
                addSessionDto.getCoachName(),
                addSessionDto.getMaxPeople());

        session.setAdmin(admin);

        sessionService.saveSession(session);
    }

    public void deleteSession(DeleteSessionDto deleteSessionDto, Long id) {
        Optional<Session> sessions = sessionService.findSessionById(id);
        Session sessionToDelete = sessions.stream().filter(session -> session.getId().equals(deleteSessionDto.getId())).toList().get(0);
        sessionService.deleteSession(sessionToDelete.getId());
    }

    public List<Session> findAllSessions() {
        return sessionService.findAllSessions();
    }

    /// SessionType part ///

    public void saveSessionType(AddSessionTypeDto addSessionTypeDto) {
        SessionType sessionType = new SessionType(addSessionTypeDto.getName());
        sessionTypeService.saveSessionType(sessionType);
    }

    public void deleteSessionType(Long id) {
        sessionTypeService.deleteSessionType(id);
    }

    public List<SessionType> findAllSessionTypes() {
        return sessionTypeService.findAllSessionTypes();
    }

    /// User part ///

    @Transactional
    public String registerUser(AuthRequest authRequest) {
        if (memberService.existsByEmail(authRequest.getEmail())) {
            throw new IllegalArgumentException("Error: Email is already taken!");
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

        Role roles = roleService.findByName("MEMBER");
        member.setRoles(Collections.singletonList(roles));

        memberService.save(member);

        return "User registered successfully!";

    }

    public AuthResponseDto login(LoginRequest loginRequest, AuthenticationManager authenticationManager) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getEmail());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDetails.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return new AuthResponseDto(token);
    }


    /// Admin part ///

    @Transactional
    public String registerAdmin(AuthRequest authRequest) {
        if (adminService.existsByEmail(authRequest.getEmail())) {
            throw new IllegalArgumentException("Error: Email is already taken!");
        }

        Admin admin = new Admin();
        admin.setEmail(authRequest.getEmail());
        admin.setPassword(passwordEncoder.encode(authRequest.getPassword()));

        Role roles = roleService.findByName("ADMIN");
        admin.setRoles(Collections.singletonList(roles));

        adminService.save(admin);

        return "Admin registered successfully!";
    }

}
