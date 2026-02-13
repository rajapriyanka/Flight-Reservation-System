package com.flight.util;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	@Value("${jwt.secret}")
	private String secretKey;

	@Value("${jwt.expiration}")
	private long expirationTime;

	private Key getSigningKey() {
		return Keys.hmacShaKeyFor(secretKey.getBytes());
	}

	public String generateToken(String username) {
		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expirationTime)).signWith(getSigningKey())
				.compact();
	}

	private Claims extractAllClaims(String jwtToken) {
		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(jwtToken).getBody();
	}

	public String extractUsername(String jwtToken) {
		return extractAllClaims(jwtToken).getSubject();
	}

	public Boolean isTokenExpired(String jwtToken) {
		return extractAllClaims(jwtToken).getExpiration().before(new Date());
	}

	public boolean validateToken(String jwtToken, UserDetails userDetails) {
		final String username = extractUsername(jwtToken);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken);
	}
}
