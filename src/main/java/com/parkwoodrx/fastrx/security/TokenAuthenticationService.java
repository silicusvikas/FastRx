package com.parkwoodrx.fastrx.security;

import java.util.Collections;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenAuthenticationService {
	
	private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationService.class);
	
	private static final long EXPIRATIONTIME = 432_000_000; // 10 days
	private static final String SECRET = "adsdfgkj@,gmh,@@@dfgdfgh#2D(*jlj@#djl,ssdfgsdfkj<.,m";
	private static final String TOKEN_PREFIX = "Bearer";
	private static final String HEADER_STRING = "Authorization";

	public void addAuthentication(HttpServletResponse res, String username) {
		logger.info("in TokenAuthenticationService:: addAuthentication method ");
		String JWT = Jwts.builder().setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();
		res.addHeader(HEADER_STRING, TOKEN_PREFIX+ JWT);
	}

	public Authentication getAuthentication(HttpServletRequest request) {
		logger.info("in TokenAuthenticationService:: getAuthentication method ");
		String token = request.getHeader(HEADER_STRING);
		logger.info("token is: "+ token);
		if (token != null && token.startsWith(TOKEN_PREFIX)) {
			String user = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody()
					.getSubject();
			return user != null ? new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList()) : null;
		}
		return null;
	}
}
