package br.com.ifc.auth.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import br.com.ifc.auth.model.HeaderHttp;
import br.com.ifc.auth.service.AuthService;

/**
 * Filter JWT JWTAuthenticationFilter
 * @author thiago.colombo
 *
 */
@Component
public class JWTAuthenticationFilter extends GenericFilterBean {
	
	@Autowired
	private AuthService authService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {	
		String token = ((HttpServletRequest) request).getHeader(HeaderHttp.AUTHORIZATION.getName());
		Authentication authentication = authService.getAuthentication(token);		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		filterChain.doFilter(request, response);		
	}	
}
