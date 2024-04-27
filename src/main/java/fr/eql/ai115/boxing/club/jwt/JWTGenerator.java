package fr.eql.ai115.boxing.club.jwt;


import fr.eql.ai115.boxing.club.entity.Admin;
import fr.eql.ai115.boxing.club.entity.Member;
import fr.eql.ai115.boxing.club.service.impl.AdminService;
import fr.eql.ai115.boxing.club.service.impl.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Component
public class JWTGenerator {

    @Autowired
    AdminService adminService;

    @Autowired
    MemberService memberService;

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();

        Long userId;
        Optional<Admin> adminOptional = adminService.findByEmail(username);
        if (adminOptional.isPresent()) {
            userId = adminOptional.get().getId();
        } else {
            Member member = memberService.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            userId = member.getId();
        }

        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);

        String token = Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .claim("roles", authentication.getAuthorities())
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        System.out.println("New key : ");
        System.out.println(key);
        System.out.println("New token : ");
        System.out.println(token);
        return token;
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("userId", Long.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect", e.fillInStackTrace());
        }
    }
}
