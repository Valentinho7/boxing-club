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
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    ReservationService reservationService;

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

    ////////////////////////
    /// Session methods ///
    ///////////////////////

    /**
     * Saves a new session.
     *
     * This method takes an AddSessionDto object as a parameter, which contains the details of the session to be saved.
     * It retrieves the current authenticated admin from the SecurityContext, and finds the corresponding admin in the
     * database.
     * It then creates a new Session object with the details from the AddSessionDto, and sets the admin of the session
     * to the authenticated admin.
     * Finally, it saves the new session in the database.
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
     * This method takes the ID of a session as a parameter, finds the corresponding session in the database, and deletes it.
     * If no session is found with the provided ID, it throws an IllegalArgumentException.
     *
     * @param id The ID of the session to delete.
     * @throws IllegalArgumentException If no session is found with the provided ID.
     */
    public void deleteSession(Long id) {
        Session sessionToDelete = sessionService.findSessionById(id)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));

        sessionService.deleteSession(sessionToDelete.getId());
    }

    /**
     * Retrieves all sessions.
     *
     * This method retrieves all sessions from the database and returns them as a list.
     *
     * @return A list of all sessions.
     */
    public List<Session> findAllSessions() {
        return sessionService.findAllSessions();
    }

    /**
     * Updates a session.
     *
     * This method takes an AddSessionDto object and a session ID as parameters.
     * The AddSessionDto object contains the updated details of the session.
     * The method first finds the session by its ID.
     * If the session is not found, it throws an IllegalArgumentException.
     * If the session is found, it updates the session's details with the details from the AddSessionDto, and saves the updated session in the database.
     *
     * @param updateSessionDto The session data transfer object containing the updated session details.
     * @param id The ID of the session to update.
     * @throws IllegalArgumentException If no session is found with the provided ID.
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

    @org.springframework.transaction.annotation.Transactional
    public List<SessionDto> findSessionsByReservationId(Long reservationId) {
        Reservation reservation = reservationService.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        List<Session> sessions = reservation.getSessions();
        return sessions.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private SessionDto mapToDto(Session session) {
        SessionDto dto = new SessionDto();
        dto.setId(session.getId());
        dto.setName(session.getName());
        dto.setDurationInHours(session.getDurationInHours());
        dto.setSessionType(session.getSessionType());
        dto.setDate(session.getDate());
        dto.setHour(session.getHour());
        dto.setCoachName(session.getCoachName());
        dto.setMaxPeople(session.getMaxPeople());
        dto.setDescription(session.getDescription());
        return dto;
    }

    ///////////////////////////
    /// SessionType methods ///
    ///////////////////////////

    /**
     * Saves a new session type.
     *
     * This method takes an AddSessionTypeDto object as a parameter, which contains the details of the session type to be saved.
     * It creates a new SessionType object with the details from the AddSessionTypeDto, and saves the new session type in the database.
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
     * This method takes the ID of a session type as a parameter, finds the corresponding session type in the database, and deletes it.
     * If no session type is found with the provided ID, it throws an IllegalArgumentException.
     * If the session type is being used by any session, it throws an IllegalStateException.
     *
     * @param id The ID of the session type to delete.
     * @throws IllegalArgumentException If no session type is found with the provided ID.
     * @throws IllegalStateException If the session type is being used by any session.
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
     * This method takes an AddSessionTypeDto object and a session type ID as parameters.
     * The AddSessionTypeDto object contains the updated details of the session type.
     * The method first finds the session type by its ID.
     * If the session type is not found, it throws an IllegalArgumentException.
     * If the session type is found, it updates the session type's details with the details from the AddSessionTypeDto, and saves the updated session type in the database.
     *
     * @param updateSessionTypeDto The session type data transfer object containing the updated session type details.
     * @param id The ID of the session type to update.
     * @throws IllegalArgumentException If no session type is found with the provided ID.
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
     * This method retrieves all session types from the database and returns them as a list.
     *
     * @return A list of all session types.
     */
    public List<SessionType> findAllSessionTypes() {
        return sessionTypeService.findAllSessionTypes();
    }



    ////////////////////////////
    /// Reservation methods ///
    //////////////////////////

    public void registerOrder(Long memberId, List<Long> sessionIds) {
        Member member = memberService.getMemberById(memberId);

        List<Session> sessions = sessionIds.stream()
                .map(sessionService::getSessionById)
                .collect(Collectors.toList());

        Reservation reservation = new Reservation();
        reservation.setOrderedDate(LocalDate.now());
        reservation.setMember(member);
        reservation.setSessions(sessions);

        reservationService.saveReservation(reservation);
    }

    public List<ReservationDto> findAllReservationsByMembers(Long memberId) {
        List<Reservation> reservations = memberService.getMemberReservations(memberId);
        return reservations.stream()
                .map(reservation -> {
                    ReservationDto dto = new ReservationDto();
                    dto.setId(reservation.getId());
                    dto.setOrderedDate(reservation.getOrderedDate());
                    dto.setValidateDate(reservation.getValidateDate());
                    dto.setValidate(reservation.isValidate());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<ReservationDto> findAllValidateReservations() {
        List<Reservation> reservations = reservationService.findAllValidateReservations();
        return reservations.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private ReservationDto mapToDto(Reservation reservation) {
        ReservationDto dto = new ReservationDto();
        dto.setId(reservation.getId());
        dto.setOrderedDate(reservation.getOrderedDate());
        dto.setValidateDate(reservation.getValidateDate());
        dto.setValidate(reservation.isValidate());
        return dto;
    }

    public void validateReservation(Long reservationId) {
        reservationService.validateReservation(reservationId);
    }

    ///////////////////////
    /// Member methods ///
    /////////////////////

    /**
     * Registers a new user.
     *
     * This method takes an AddMemberDto object as a parameter, which contains the details of the user to be registered.
     * It checks if a user already exists with the provided email. If so, it throws an IllegalArgumentException.
     * Otherwise, it creates a new Member object with the details from the AddMemberDto, sets the role of the member
     * to "MEMBER", and saves the new member in the database.
     * Finally, it returns a success message.
     *
     * @param addMemberDto The authentication request containing the user details.
     * @return A string message indicating the result of the registration.
     * @throws IllegalArgumentException If a user already exists with the provided email.
     */
    @Transactional
    public String registerUser(AddMemberDto addMemberDto) {
        if (memberService.existsByEmail(addMemberDto.getEmail())) {
            throw new IllegalArgumentException("Error: Email is already taken!");
        }

        if (addMemberDto.getFirstname() == null || addMemberDto.getFirstname().isEmpty()) {
            throw new IllegalArgumentException("Error: Firstname cannot be empty!");
        }

        if (addMemberDto.getLastname() == null || addMemberDto.getLastname().isEmpty()) {
            throw new IllegalArgumentException("Error: Lastname cannot be empty!");
        }

        if (addMemberDto.getBirthdate() == null) {
            throw new IllegalArgumentException("Error: Birthdate cannot be empty!");
        }

        if (addMemberDto.getEmail() == null || addMemberDto.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Error: Email cannot be empty!");
        }

        if (addMemberDto.getPhoneNumber() == null || addMemberDto.getPhoneNumber().isEmpty()) {
            throw new IllegalArgumentException("Error: Phone number cannot be empty!");
        }

        if (addMemberDto.getAddress() == null || addMemberDto.getAddress().isEmpty()) {
            throw new IllegalArgumentException("Error: Address cannot be empty!");
        }

        if (addMemberDto.getPassword() == null || addMemberDto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Error: Password cannot be empty!");
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
     * This method takes an AddMemberDto object and a member ID as parameters.
     * The AddMemberDto object contains the updated details of the member.
     * The method first finds the member by their ID.
     * If the member is not found, it throws an IllegalArgumentException.
     * If the member is found, it updates the member's details with the details from the AddMemberDto, and saves the
     * mupdated member in the database.
     *
     * @param updateMemberDto The data transfer object containing the updated member details.
     * @param id The ID of the member to update.
     * @throws IllegalArgumentException If no member is found with the provided ID.
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

        memberService.save(memberToUpdate);
    }

    /**
     * Changes the password of a member.
     *
     * This method takes the member's ID and a PasswordChangeRequestDto object as parameters.
     * The PasswordChangeRequestDto object contains the old password, the new password, and the confirmation of the new password.
     * The method first checks if the new password and the confirmation match.
     * If they do not match, it throws an IllegalArgumentException.
     * Then, it checks if the old password is correct.
     * If the old password is incorrect, it throws an IllegalArgumentException.
     * If both checks pass, it updates the member's password in the database.
     *
     * @param memberId The ID of the member whose password is to be changed.
     * @param passwordChangeRequest The PasswordChangeRequestDto object containing the old password, the new password, and the confirmation of the new password.
     * @throws IllegalArgumentException If the new password and the confirmation do not match, or if the old password is incorrect.
     */
    public void changeMemberPassword(Long memberId, PasswordChangeRequestDto passwordChangeRequest) {
        String oldPassword = passwordChangeRequest.getOldPassword();
        String newPassword = passwordChangeRequest.getNewPassword();
        String confirmPassword = passwordChangeRequest.getConfirmPassword();

        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("New password and confirmation do not match");
        }

        if (!passwordEncoder.matches(oldPassword, memberService.getMemberPassword(memberId))) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        memberService.updateMemberPassword(memberId, passwordEncoder.encode(newPassword));
    }

    public void validatePaymentAndSubscription(Long memberId) {
        Member member = memberService.findMemberById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        memberService.validatePayment(memberId);

        // Retrieve the member object again after validating the payment
        member = memberService.findMemberById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        if (!member.isPayementValidated()) {
            throw new IllegalArgumentException("Payment must be validated before validating subscription");
        }

        memberService.validateSubscription(memberId);
    }

    public ShowMemberDto getMemberDetails(Long id) {
        Member member = memberService.getMemberById(id);
        return convertToDto(member);
    }

    private ShowMemberDto convertToDto(Member member) {
        ShowMemberDto dto = new ShowMemberDto();
        dto.setEmail(member.getEmail());
        dto.setAddress(member.getAddress());
        dto.setBirthdate(member.getBirthdate());
        dto.setFirstname(member.getFirstname());
        dto.setLastname(member.getLastname());
        dto.setPhoneNumber(member.getPhoneNumber());
        return dto;
    }


    //////////////////////
    /// Admin methods ///
    ////////////////////

    /**
     * Registers a new admin.
     *
     * This method takes an AddMemberDto object as a parameter, which contains the details of the admin to be registered.
     * It checks if an admin already exists with the provided email. If so, it throws an IllegalArgumentException.
     * Otherwise, it creates a new Admin object with the details from the AddMemberDto, sets the role of the admin to "ADMIN", and saves the new admin in the database.
     * Finally, it returns a success message.
     *
     * @param addMemberDto The authentication request containing the admin details.
     * @return A string message indicating the result of the registration.
     * @throws IllegalArgumentException If an admin already exists with the provided email.
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

    /**
     * Updates the email of an existing admin.
     *
     * This method takes an UpdateAdminDto object and an admin ID as parameters.
     * The UpdateAdminDto object contains the new email of the admin.
     * The method first finds the admin by their id.
     * If the admin is not found, it throws an IllegalArgumentException.
     * If the admin is found, it updates the admin email in the database.
     *
     * @param updateAdminDto The UpdateAdminDto object containing the new email of the admin.
     * @param id The ID of the admin to update.
     * @throws IllegalArgumentException If no admin is found with the provided email.
     */
    public void updateAdmin(UpdateAdminDto updateAdminDto, Long id) {
        Admin adminToUpdate = adminService.findAdminById(id)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));

        if (updateAdminDto.getEmail() != null) {
            adminToUpdate.setEmail(updateAdminDto.getEmail());
        }

        adminService.save(adminToUpdate);
    }

    /**
     * Changes the password of an admin.
     *
     * This method takes the admins ID and a PasswordChangeRequestDto object as parameters.
     * The PasswordChangeRequestDto object contains the old password, the new password, and the confirmation of the new password.
     * The method first checks if the new password and the confirmation match.
     * If they do not match, it throws an IllegalArgumentException.
     * Then, it checks if the old password is correct.
     * If the old password is incorrect, it throws an IllegalArgumentException.
     * If both checks pass, it updates the admin password in the database.
     *
     * @param adminId The ID of the admin whose password is to be changed.
     * @param passwordChangeRequest The PasswordChangeRequestDto object containing the old password, the new password, and the confirmation of the new password.
     * @throws IllegalArgumentException If the new password and the confirmation do not match, or if the old password is incorrect.
     */
    public void changeAdminPassword(Long adminId, PasswordChangeRequestDto passwordChangeRequest) {
        String oldPassword = passwordChangeRequest.getOldPassword();
        String newPassword = passwordChangeRequest.getNewPassword();
        String confirmPassword = passwordChangeRequest.getConfirmPassword();

        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("New password and confirmation do not match");
        }

        if (!passwordEncoder.matches(oldPassword, adminService.getAdminPassword(adminId))) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        adminService.updateAdminPassword(adminId, passwordEncoder.encode(newPassword));
    }

    ///////////////////////////////
    /// Authentication methods ///
    /////////////////////////////

    /**
     * Authenticates a user.
     *
     * This method takes a LoginRequest object and an AuthenticationManager as parameters.
     * The LoginRequest object contains the user's credentials (email and password).
     * The method first retrieves the UserDetails of the user by their email.
     * It then authenticates the user using the AuthenticationManager.
     * If the authentication is successful, it sets the authentication in the SecurityContext.
     * It then generates a JWT token for the authenticated user using the JWTGenerator.
     * Finally, it returns an AuthResponseDto object containing the generated token.
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
