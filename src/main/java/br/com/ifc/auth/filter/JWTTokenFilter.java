package br.com.ifc.auth.filter;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import br.com.ifc.auth.model.HeaderHttp;
import br.com.ifc.auth.model.Token;
import br.com.ifc.auth.model.UserCredential;
import br.com.ifc.auth.service.AuthService;

/**
 * Filter JWT Token
 * 
 * @author thiago.colombo
 *
 */
@Component
public class JWTTokenFilter extends AbstractAuthenticationProcessingFilter {

	private static final String CONTENT_TYPE = "application/json";

	@Autowired
	private AuthService authService;

	/**
	 * Construct
	 * 
	 * @param url
	 * @param authManager
	 */
	public JWTTokenFilter(@Value("/token") String url, AuthenticationManager authManager) {
		super(new AntPathRequestMatcher(url));
		setAuthenticationManager(authManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		UserCredential userCredential = new ObjectMapper().readValue(request.getInputStream(), UserCredential.class);
		AuthenticationManager authenticationManager = getAuthenticationManager();
		UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
				userCredential.getName(), userCredential.getPassword(), Collections.emptyList());
		return authenticationManager.authenticate(userToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain, Authentication auth) throws IOException, ServletException {

		try {
			SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
			Token token = authService.getToken(auth, 860000000);
			response.addHeader(HeaderHttp.AUTHORIZATION.getName(), token.parserTokenHeader());
			response.setContentType(CONTENT_TYPE);
			response.getWriter().print(new Gson().toJson(token));
		} catch (Exception exception) {
			throw new ServletException(exception);
		}
	}
}
