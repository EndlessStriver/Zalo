package com.example.demo.service.imp;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.entities.enums.TokenType;
import com.example.demo.exception.BadRequestException;
import com.example.demo.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImp implements JwtService {

	@Value("${jwt.secretKey}")
	private String secretKey;

	private Key getSigningKey() {
		byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	@Override
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	@Override
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private String extractTokenType(String token) {
		Claims claims = extractAllClaims(token);
		return claims.get("type", String.class);
	}

	@Override
	public boolean isAccessToken(String token) {
		String tokenType = extractTokenType(token);
		return TokenType.ACCESS.equals(TokenType.valueOf(tokenType));
	}

	@Override
	public boolean isRefreshToken(String token) {
		String tokenType = extractTokenType(token);
		return TokenType.REFRESH.equals(TokenType.valueOf(tokenType));
	}

	private String createToken(Map<String, Object> claims, String subject, int hour) {
		LocalDateTime now = LocalDateTime.now();
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(Date.from(now.plusHours(hour).atZone(ZoneId.systemDefault()).toInstant()))
				.signWith(getSigningKey(), SignatureAlgorithm.HS512).compact();
	}

	@Override
	public String generateToken(String username, TokenType tokenType) {
		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("type", tokenType.toString());
		return createToken(claims, username, tokenType.equals(TokenType.ACCESS) ? 1 : 24);
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	@Override
	public boolean validateToken(String token, String username) {
		final String extractedUsername = extractUsername(token);
		return (extractedUsername.equals(username) && !isTokenExpired(token));
	}

	@Override
	public String refreshToken(String refreshToken) {
		if (!isRefreshToken(refreshToken) || isTokenExpired(refreshToken))
			throw new BadRequestException("Refresh Token không hợp lệ hoặc đã hết hạn");
		String username = extractUsername(refreshToken);
		return generateToken(username, TokenType.ACCESS);
	}
}
