package br.com.ifc.auth.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import br.com.ifc.auth.config.AuthConfig;
import br.com.ifc.auth.model.HeaderHttp;
import br.com.ifc.auth.vo.AuthTokenVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class AuthenticationService {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

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
	public AuthTokenVO getToken(UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) {
		Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
		return new AuthTokenVO(createToken(authentication, config.getExpirationTime()), HeaderHttp.TOKEN_PREFIX.getName());
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
		
		LOGGER.info("Token created to user " + auth.getName() + "!");
		
		return token;
	}
	
	/**
	 * 
	 * @param token
	 * @return
	 */
	private Jws<Claims> parseClaimsJwsToken(String token) {
		JwtParser jwtParser = Jwts.parser().setSigningKey(config.getSecret());
        return jwtParser.parseClaimsJws(token);
	}
	
	/**
	 * 
	 * @param token
	 * @return
	 */
	public String getUsernameFromToken(String token) {

		if (token != null) {

			return parseClaimsJwsToken(token).getBody().getSubject();
		}
		
		return null;
	}        
	
	/**
	 * 
	 * @param token
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public UsernamePasswordAuthenticationToken getAuthentication(final String token) {
        Jws<Claims> claimsJws = parseClaimsJwsToken(token);
        Claims claims = claimsJws.getBody();
        String user = claims.getSubject();        
		ArrayList<String> roles = (ArrayList<String>)claims.get(AUTHORITIES_KEY);
        
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if(roles != null) {
            for (String role : roles) {
            	SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role);
            	authorities.add(simpleGrantedAuthority);
            }
        }
                
        return new UsernamePasswordAuthenticationToken(user, "", authorities);
    }	
}
