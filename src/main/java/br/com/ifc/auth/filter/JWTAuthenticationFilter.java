package br.com.ifc.auth.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.ifc.auth.model.HeaderHttp;
import br.com.ifc.auth.service.AuthenticationService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

/**
 * Filter JWT JWTAuthenticationFilter
 * 
 * @author thiago.colombo
 *
 */
@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private AuthenticationService authenticationService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String token = request.getHeader(HeaderHttp.AUTHORIZATION.getName());
		String username = null;

		if (token != null) {
			try {
				token = token.replace(HeaderHttp.TOKEN_PREFIX.getName(), "").trim();
				username = authenticationService.getUsernameFromToken(token);

			} catch (IllegalArgumentException e) {
				throw new ServletException("An error occured during getting username from token", e);
			} catch (ExpiredJwtException e) {
				throw new ServletException("The token is expired and not valid anymore", e);
			} catch (SignatureException e) {
				throw new ServletException("Authentication Failed. Username or Password not valid.");
			}
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UsernamePasswordAuthenticationToken authentication = authenticationService.getAuthentication(token);
			
			System.out.println("Autenticacao: " + authentication);
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authentication);			
		}

		filterChain.doFilter(request, response);
	}
}
