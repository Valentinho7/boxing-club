package fr.eql.ai115.boxing.club.controller.rest;

import fr.eql.ai115.boxing.club.entity.Member;
import fr.eql.ai115.boxing.club.entity.dto.AuthRequest;
import fr.eql.ai115.boxing.club.entity.dto.AuthResponse;
import fr.eql.ai115.boxing.club.exception.AccountExistsException;
import fr.eql.ai115.boxing.club.exception.UnauthorizedException;
import fr.eql.ai115.boxing.club.service.SecurityService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/security")
public class SecurityRestController {

    private SecurityService securityService;

    @PostMapping("/authorize")
    public ResponseEntity<AuthResponse> authorize(@RequestBody AuthRequest authRequest) throws UnauthorizedException {
        Authentication authentication;
        try {
            authentication = securityService.authenticate(authRequest.getUsername(), authRequest.getPassword());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Member member = (Member) authentication.getPrincipal();
            String token = securityService.generateJsonWebTokenForMember(member);
            return ResponseEntity.ok(new AuthResponse(member, token));
        } catch(AuthenticationException e) {
            throw new UnauthorizedException();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest requestDto) throws AccountExistsException {
        Member user = securityService.save(
                requestDto.getUsername(),
                requestDto.getPassword(),
                requestDto.getEmail(),
                requestDto.getAddress(),
                requestDto.getBirthdate(),
                requestDto.getFirstname(),
                requestDto.getLastname(),
                requestDto.getPhoneNumber()
        );
        String token = securityService.generateJsonWebTokenForMember(user);
        return ResponseEntity.ok(new AuthResponse(user, token));
    }

}
