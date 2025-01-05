package com.example.demo.service;
import java.util.Date;

import com.example.demo.entities.enums.TokenType;


public interface JwtService {

	public String extractUsername(String token);

	public Date extractExpiration(String token);
	
	public boolean isAccessToken(String token);
	
	public boolean isRefreshToken(String token);

	public String generateToken(String username, TokenType tokenType);
	
	public boolean validateToken(String token, String username);
	
	public String refreshToken(String refreshToken);
	
}
