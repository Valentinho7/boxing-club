package fr.eql.ai115.boxing.club.service.impl;

import fr.eql.ai115.boxing.club.entity.Member;
import fr.eql.ai115.boxing.club.exception.AccountExistsException;
import fr.eql.ai115.boxing.club.repository.MemberDao;
import fr.eql.ai115.boxing.club.service.SecurityService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;

@Service
@Configuration
public class SecurityServiceImpl implements SecurityService {

    private MemberDao memberDao;
    private AuthenticationManager authenticationManager;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private final String signingKey;

    public SecurityServiceImpl(@Value("${jwt.signing.key}") String signingKey) {
        this.signingKey = signingKey;
    }

    /// Used for registration
    @Override
    public Member save(String username, String password, String email, String address, LocalDate birthdate, String firstname, String lastname, String phoneNumber) throws AccountExistsException {
        if (memberDao.findByEmail(username) != null) {
            throw new AccountExistsException();
        }
        Member member = new Member();
        member.setEmail(username);
        member.setPassword(passwordEncoder().encode(password));
        member.setEmail(email);
        member.setAddress(address);
        member.setBirthdate(birthdate);
        member.setFirstname(firstname);
        member.setLastname(lastname);
        member.setPhoneNumber(phoneNumber);
        member.setRegistrationDate(LocalDate.now());
        memberDao.save(member);
        return member;
    }


    /// Used for authentication
    public Member getMemberFromJsonWebToken(String token) {
        String username = getUsernameFromToken(token);
        return (Member) loadUserByUsername(username);
    }

    private String getUsernameFromToken(String token) {
        System.out.println(signingKey);
        Claims claims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberDao.findByEmail(username);
        if (member == null) {
            throw new UsernameNotFoundException("The owner could not be found");
        }
        return member;
    }

    @Override
    public String generateJsonWebTokenForMember(Member member) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 3600 * 1000);
        return Jwts.builder().setSubject(member.getUsername()).setIssuedAt(now).setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, signingKey)
                .compact();
    }

    @Override
    public Authentication authenticate(String email, String password) throws AuthenticationException {
        return null;
    }


}
