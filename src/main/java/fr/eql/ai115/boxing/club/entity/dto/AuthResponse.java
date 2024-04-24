package fr.eql.ai115.boxing.club.entity.dto;

import org.springframework.security.core.userdetails.UserDetails;

public class AuthResponse {

	private UserDetails member;
	private String token;

    public AuthResponse(UserDetails member, String token) {
        this.member = member;
        this.token = token;
    }

	public UserDetails getOwner() {
		return member;
	}
	public String getToken() {
		return token;
	}
}
