package fr.eql.ai115.boxing.club.service.impl;

import fr.eql.ai115.boxing.club.entity.*;
import fr.eql.ai115.boxing.club.entity.dto.*;
import fr.eql.ai115.boxing.club.jwt.JWTGenerator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * Main service class for the application.
 *
 * @author Valentin Claquin
 */
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

    /// Session methods ///

    /**
     * Saves a new session.
     *
     * @param addSessionDto The session data transfer object containing the session details.
     */
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

    /**
     * Deletes a session.
     *
     * @param id The ID of the session to delete.
     */
    public void deleteSession(Long id) {
        Session sessionToDelete = sessionService.findSessionById(id)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));

        sessionService.deleteSession(sessionToDelete.getId());
    }

    /**
     * Retrieves all sessions.
     *
     * @return A list of all sessions.
     */
    public List<Session> findAllSessions() {
        return sessionService.findAllSessions();
    }

    /**
     * Updates a session.
     *
     * @param updateSessionDto The session data transfer object containing the updated session details.
     * @param id The ID of the session to update.
     */
    public void updateSession(AddSessionDto updateSessionDto, Long id) {
        Session sessionToUpdate = sessionService.findSessionById(id)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));

        if (updateSessionDto.getName() != null) {
            sessionToUpdate.setName(updateSessionDto.getName());
        }
        if (updateSessionDto.getDurationInHours() != 0) {
            sessionToUpdate.setDurationInHours(updateSessionDto.getDurationInHours());
        }
        if (updateSessionDto.getDescription() != null) {
            sessionToUpdate.setDescription(updateSessionDto.getDescription());
        }
        if (updateSessionDto.getNameSessionType() != null) {
            SessionType sessionType;
            if (sessionTypeService.existsByName(updateSessionDto.getNameSessionType())) {
                sessionType = sessionTypeService.findByName(updateSessionDto.getNameSessionType()).get(0);
            } else {
                sessionType = new SessionType(updateSessionDto.getNameSessionType());
                sessionTypeService.saveSessionType(sessionType);
            }
            sessionToUpdate.setSessionType(sessionType);
        }
        if (updateSessionDto.getDate() != null) {
            sessionToUpdate.setDate(updateSessionDto.getDate());
        }
        if (updateSessionDto.getHour() != 0) {
            sessionToUpdate.setHour(updateSessionDto.getHour());
        }
        if (updateSessionDto.getCoachName() != null) {
            sessionToUpdate.setCoachName(updateSessionDto.getCoachName());
        }
        if (updateSessionDto.getMaxPeople() != 0) {
            sessionToUpdate.setMaxPeople(updateSessionDto.getMaxPeople());
        }

        sessionService.saveSession(sessionToUpdate);
    }

    /// SessionType methods ///

    /**
     * Saves a new session type.
     *
     * @param addSessionTypeDto The session type data transfer object containing the session type details.
     */
    public void saveSessionType(AddSessionTypeDto addSessionTypeDto) {
        SessionType sessionType = new SessionType(addSessionTypeDto.getName());
        sessionTypeService.saveSessionType(sessionType);
    }

    /**
     * Deletes a session type.
     *
     * @param id The ID of the session type to delete.
     */
    public void deleteSessionType(Long id) {
        SessionType sessionTypeToDelete = sessionTypeService.findSessionTypeById(id)
                .orElseThrow(() -> new IllegalArgumentException("SessionType not found"));

        List<Session> sessionsUsingSessionType = sessionService.findSessionsBySessionType(sessionTypeToDelete);

        if (!sessionsUsingSessionType.isEmpty()) {
            throw new IllegalStateException("Cannot delete SessionType. It is currently being used by a Session.");
        }

        sessionTypeService.deleteSessionType(sessionTypeToDelete.getId());
    }

    /**
     * Updates a session type.
     *
     * @param updateSessionTypeDto The session type data transfer object containing the updated session type details.
     * @param id The ID of the session type to update.
     */
    public void updateSessionType(AddSessionTypeDto updateSessionTypeDto, Long id) {
        SessionType sessionTypeToUpdate = sessionTypeService.findSessionTypeById(id)
                .orElseThrow(() -> new IllegalArgumentException("SessionType not found"));

        if (updateSessionTypeDto.getName() != null) {
            if (sessionTypeService.existsByName(updateSessionTypeDto.getName())) {
                throw new IllegalArgumentException("SessionType name already exists");
            }
            sessionTypeToUpdate.setName(updateSessionTypeDto.getName());
        }

        sessionTypeService.saveSessionType(sessionTypeToUpdate);
    }

    /**
     * Retrieves all session types.
     *
     * @return A list of all session types.
     */
    public List<SessionType> findAllSessionTypes() {
        return sessionTypeService.findAllSessionTypes();
    }

    /// User methods ///

    /**
     * Registers a new user.
     *
     * @param addMemberDto The authentication request containing the user details.
     * @return A string message indicating the result of the registration.
     */
    @Transactional
    public String registerUser(AddMemberDto addMemberDto) {
        if (memberService.existsByEmail(addMemberDto.getEmail())) {
            throw new IllegalArgumentException("Error: Email is already taken!");
        }

        Member member = new Member();
        member.setFirstname(addMemberDto.getFirstname());
        member.setLastname(addMemberDto.getLastname());
        member.setBirthdate(addMemberDto.getBirthdate());
        member.setEmail(addMemberDto.getEmail());
        member.setPhoneNumber(addMemberDto.getPhoneNumber());
        member.setAddress(addMemberDto.getAddress());
        member.setRegistrationDate(LocalDate.now());
        member.setPassword(passwordEncoder.encode(addMemberDto.getPassword()));

        Role roles = roleService.findByName("MEMBER");
        member.setRoles(Collections.singletonList(roles));

        memberService.save(member);

        return "User registered successfully!";

    }

    /**
     * Updates the details of an existing member.
     *
     * @param updateMemberDto The data transfer object containing the updated member details.
     * @param id The ID of the member to update.
     * @throws IllegalArgumentException if no member is found with the provided ID.
     */
    public void updateMember(AddMemberDto updateMemberDto, Long id) {
        Member memberToUpdate = memberService.findMemberById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        if (updateMemberDto.getFirstname() != null) {
            memberToUpdate.setFirstname(updateMemberDto.getFirstname());
        }
        if (updateMemberDto.getLastname() != null) {
            memberToUpdate.setLastname(updateMemberDto.getLastname());
        }
        if (updateMemberDto.getBirthdate() != null) {
            memberToUpdate.setBirthdate(updateMemberDto.getBirthdate());
        }
        if (updateMemberDto.getEmail() != null) {
            memberToUpdate.setEmail(updateMemberDto.getEmail());
        }
        if (updateMemberDto.getPhoneNumber() != null) {
            memberToUpdate.setPhoneNumber(updateMemberDto.getPhoneNumber());
        }
        if (updateMemberDto.getAddress() != null) {
            memberToUpdate.setAddress(updateMemberDto.getAddress());
        }
        if (updateMemberDto.getPassword() != null) {
            memberToUpdate.setPassword(passwordEncoder.encode(updateMemberDto.getPassword()));
        }

        memberService.save(memberToUpdate);
    }

    /// Admin methods ///

    /**
     * Registers a new admin.
     *
     * @param addMemberDto The authentication request containing the admin details.
     * @return A string message indicating the result of the registration.
     */
    @Transactional
    public String registerAdmin(AddMemberDto addMemberDto) {
        if (adminService.existsByEmail(addMemberDto.getEmail())) {
            throw new IllegalArgumentException("Error: Email is already taken!");
        }

        Admin admin = new Admin();
        admin.setEmail(addMemberDto.getEmail());
        admin.setPassword(passwordEncoder.encode(addMemberDto.getPassword()));

        Role roles = roleService.findByName("ADMIN");
        admin.setRoles(Collections.singletonList(roles));

        adminService.save(admin);

        return "Admin registered successfully!";
    }

    /// Authentication methods ///

    /**
     * Authenticates a user.
     *
     * @param loginRequest The login request containing the user's credentials.
     * @param authenticationManager The authentication manager.
     * @return An authentication response data transfer object containing the authentication token.
     */
    public AuthResponseDto login(LoginRequest loginRequest, AuthenticationManager authenticationManager) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getEmail());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDetails.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return new AuthResponseDto(token);
    }
}
