package br.com.ifc.auth.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import br.com.ifc.auth.config.AuthConfig;
import br.com.ifc.auth.model.AuthToken;
import br.com.ifc.auth.model.HeaderHttp;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class AuthenticationService {

	private static final String AUTHORITIES_KEY = "Roles";

	@Autowired
	private AuthConfig config;
	
	@Autowired
    private AuthenticationManager authenticationManager;

	/**
	 * 
	 * @param auth
	 * @param expirationTime
	 */
	public AuthToken getToken(UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) {
		Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
		return new AuthToken(createToken(authentication, config.getExpirationTime()), HeaderHttp.TOKEN_PREFIX.getName());
	}

	/**
	 * 
	 * @param auth
	 * @param expirationTime
	 * @return
	 */
	private String createToken(Authentication auth, long expirationTime) {
		Date date = new Date();

		Set<String> roles = new HashSet<String>();
		for (GrantedAuthority grantedAuthority : auth.getAuthorities()) {
			roles.add(grantedAuthority.getAuthority());
		}

		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put(AUTHORITIES_KEY, roles);

		String token = Jwts.builder().setClaims(claims).setSubject(auth.getName()).setIssuer("auth-ms")
				.setExpiration(new Date(date.getTime() + expirationTime)).setIssuedAt(date).setHeaderParam("typ", "JWT")
				.signWith(SignatureAlgorithm.HS512, config.getSecret()).compact();
		return token;
	}
	
	/**
	 * 
	 * @param token
	 * @return
	 */
	public String getUsernameFromToken(String token) {

		if (token != null) {

			return Jwts.parser().setSigningKey(config.getSecret()).parseClaimsJws(token.replace(HeaderHttp.TOKEN_PREFIX.getName(), "")).getBody().getSubject();
		}
		
		return null;
	}        
	
	/**
	 * 
	 * @param token
	 * @return
	 */
	public UsernamePasswordAuthenticationToken getAuthentication(final String token) {
        JwtParser jwtParser = Jwts.parser().setSigningKey(config.getSecret());
        Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        final Collection<GrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(getUsernameFromToken(token), "", authorities);
    }
}
