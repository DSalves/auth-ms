package br.com.ifc.auth.service;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import br.com.ifc.auth.config.AuthConfig;
import br.com.ifc.auth.model.Token;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class AuthService {

	private static final String TOKEN_PREFIX = "Bearer";

	@Autowired
	private AuthConfig config;

	/**
	 * 
	 * @param auth
	 * @param expirationTime
	 * @throws Exception
	 */
	public Token getToken(Authentication auth, long expirationTime) throws Exception {
		return new Token(createToken(auth, expirationTime), TOKEN_PREFIX.toLowerCase());
	}

	/**
	 * 
	 * @param auth
	 * @param expirationTime
	 * @return
	 * @throws Exception
	 */
	private String createToken(Authentication auth, long expirationTime) throws Exception {
		Date date = new Date();
		
		Set<String> roles = new HashSet<String>();		
		for(GrantedAuthority grantedAuthority : auth.getAuthorities()) {
			roles.add(grantedAuthority.getAuthority());
		}
		
		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("roles", roles);

		String token = Jwts.builder().setClaims(claims)
									 .setSubject(auth.getName())
									 .setIssuer("auth-ms")
									 .setExpiration(new Date(date.getTime() + expirationTime))
									 .setIssuedAt(date)
									 .setHeaderParam("typ", "JWT")									 
									 .signWith(SignatureAlgorithm.HS512, config.getSecret()).compact();
		return token;
	}

	/**
	 * 
	 * @param token
	 * @return
	 */
	public Authentication getAuthentication(String token) {

		if (token != null) {

			String user = Jwts.parser().setSigningKey(config.getSecret())
					.parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody().getSubject();

			if (user != null) {
				return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
			}
		}

		return null;
	}
}
