package fr.eql.ai115.boxing.club.service;

import fr.eql.ai115.boxing.club.entity.Member;
import fr.eql.ai115.boxing.club.exception.AccountExistsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.time.LocalDate;

public interface SecurityService extends UserDetailsService {

    Authentication authenticate(String email, String password) throws AuthenticationException;
    Member save(String username, String password, String email, String address, LocalDate birthdate, String firstname, String lastname, String phoneNumber) throws AccountExistsException;
    String generateJsonWebTokenForMember(Member member);
    Member getMemberFromJsonWebToken(String token);
}
